// Tripping API

var TRIP_DEBUG = true;          // set false to reduce spam
var TRIP_DEBUG_TICK_THROTTLE = 20;

function dlog(msg) {
  if (TRIP_DEBUG) console.log(String(msg));
}

var DBG_LAST = {};
function dlogThrottled(key, msg) {
  if (!TRIP_DEBUG) return;
  var last = DBG_LAST[key] || -999999;
  if (Tripping.nowTick - last >= TRIP_DEBUG_TICK_THROTTLE) {
    DBG_LAST[key] = Tripping.nowTick;
    console.log(String(msg));
  }
}


// Persistent keys


var KEY_ACTIVE_TRIP_ID        = "trip_active_id";
var KEY_RUNNING_START         = "trip_running_start_tick";
var KEY_RUNNING_UNTIL         = "trip_running_until_tick";
var KEY_ACTIVE_SUBSTANCE_HASH = "trip_active_substance_hash";

var KEY_STAGE_MSG_PREFIX      = "trip_stage_msg_";
var KEY_STAGE_PART_PREFIX     = "trip_stage_part_";


// Core state


var Tripping = {
  substances: {},
  substancesByHash: {},
  antidotes: {},
  antidoteMap: {},
  nowTick: 0,
  jobs: [],

  register: function(itemId, cfg) {
    var id = String(itemId);
    cfg = cfg || {};
    cfg._kind = "substance";
    cfg._id = id;
    cfg._effectIds = collectEffectIds(cfg);

    this.substances[id] = cfg;
    this.substancesByHash[hashId(id)] = cfg;
  },

  registerAntidote: function(itemId, cfg) {
    var id = String(itemId);
    cfg = cfg || {};
    cfg._kind = "antidote";
    cfg._id = id;
    this.antidotes[id] = cfg;
  },

  scheduleAbs: function(tick, fn) {
    this.jobs.push({ at: tick, fn: fn });
  }
};

// Tick clock

function getServerTick(server) {
  try { if (server.getTickCount) return server.getTickCount(); } catch (e1) {}
  try {
    var ow = server.overworld();
    if (ow && ow.getGameTime) return ow.getGameTime();
  } catch (e2) {}
  return Tripping.nowTick + 1;
}

ServerEvents.tick(function(event) {
  Tripping.nowTick = getServerTick(event.server);

  var i = 0;
  while (i < Tripping.jobs.length) {
    var job = Tripping.jobs[i];
    if (job.at <= Tripping.nowTick) {
      Tripping.jobs.splice(i, 1);
      try { job.fn(); } catch (e) { console.log("[TRIP][JOBERR] " + e); }
    } else {
      i++;
    }
  }
});

// Utilities

function escQuotes(s) {
  return String(s).replace(/\\/g, "\\\\").replace(/"/g, '\\"');
}

function playerSelector(player) {
  return '@p[name="' + escQuotes(player.username) + '"]';
}

function runSilent(server, cmd) {
  try { return server.runCommandSilent(cmd); }
  catch (e) { return 0; }
}

// Throttled logging for failing commands
var FAIL_THROTTLE = {};
var FAIL_THROTTLE_TICKS = 40; // 2s

function runSilentChecked(server, cmd, tag) {
  var r = 0;
  try { r = server.runCommandSilent(cmd); }
  catch (e) { r = 0; }

  if (r === 0) {
    var key = (tag ? tag : "CMD") + ":" + cmd;
    var last = FAIL_THROTTLE[key] || -999999;
    if (Tripping.nowTick - last >= FAIL_THROTTLE_TICKS) {
      FAIL_THROTTLE[key] = Tripping.nowTick;
      console.log("[TRIP][CMDFAIL]" + (tag ? ("[" + tag + "]") : "") + " " + cmd);
    }
  }

  return r;
}

function tell(player, msg, color) {
  var server = player.server;
  if (!server) return;

  var safeMsg = escQuotes(msg);
  var c = color ? String(color) : "light_purple";
  var cmd = 'tellraw ' + playerSelector(player) + ' {"text":"' + safeMsg + '","color":"' + c + '"}';
  runSilent(server, cmd);
}

function clampInt(v, min, max, fallback) {
  var n = Math.floor(Number(v));
  if (isNaN(n)) n = fallback;
  if (n < min) n = min;
  if (max !== undefined && n > max) n = max;
  return n;
}

function hashId(s) {
  s = String(s);
  var h = 0;
  var i = 0;
  for (i = 0; i < s.length; i++) {
    h = ((h << 5) - h) + s.charCodeAt(i);
    h |= 0;
  }
  return h;
}

// player resolver for scheduled jobs
function resolvePlayer(server, uuidStr, username) {
  var p = null;

  // try UUID
  try {
    if (server && server.getPlayer) {
      p = server.getPlayer(String(uuidStr));
      if (p) return p;
    }
  } catch (e1) {}

  // try username
  try {
    if (server && server.getPlayer) {
      p = server.getPlayer(String(username));
      if (p) return p;
    }
  } catch (e2) {}

  // scan players list
  try {
    var list = server.players;

    if (list && list.size) {
      var n = 0;
      try { n = list.size(); } catch (e3a) { n = list.size; }

      var i = 0;
      for (i = 0; i < n; i++) {
        var q = null;
        try { q = list.get(i); } catch (e3b) { q = list[i]; }
        if (!q) continue;

        try { if (String(q.username) === String(username)) return q; } catch (e3c) {}
        try { if (String(q.uuid) === String(uuidStr)) return q; } catch (e3d) {}
      }
    } else if (list && list.length !== undefined) {
      var j = 0;
      for (j = 0; j < list.length; j++) {
        var r = list[j];
        if (!r) continue;

        try { if (String(r.username) === String(username)) return r; } catch (e4a) {}
        try { if (String(r.uuid) === String(uuidStr)) return r; } catch (e4b) {}
      }
    }
  } catch (e3) {}

  return null;
}

// Persistent data helpers

function getLong(player, key) {
  try { return player.persistentData.getLong(key); }
  catch (e) { return 0; }
}

function setLong(player, key, v) {
  try { player.persistentData.putLong(key, v); } catch (e) {}
}

function removeKey(player, key) {
  try { player.persistentData.remove(key); } catch (e) {}
}

function getBool(player, key) {
  try { return player.persistentData.getBoolean(key); }
  catch (e) { return false; }
}

function setBool(player, key, v) {
  try { player.persistentData.putBoolean(key, v === true); } catch (e) {}
}

function getActiveTripId(player) {
  return getLong(player, KEY_ACTIVE_TRIP_ID);
}

function setActiveTripId(player, tripId) {
  setLong(player, KEY_ACTIVE_TRIP_ID, tripId);
}

function clearActiveTripId(player) {
  removeKey(player, KEY_ACTIVE_TRIP_ID);
  setLong(player, KEY_ACTIVE_TRIP_ID, 0);
}

function getTripStartTick(player) {
  return getLong(player, KEY_RUNNING_START);
}

function getTripUntilTick(player) {
  return getLong(player, KEY_RUNNING_UNTIL);
}

function setTripWindow(player, startTick, untilTick) {
  setLong(player, KEY_RUNNING_START, startTick);
  setLong(player, KEY_RUNNING_UNTIL, untilTick);
}

function clearTripWindow(player) {
  removeKey(player, KEY_RUNNING_START);
  removeKey(player, KEY_RUNNING_UNTIL);
  setLong(player, KEY_RUNNING_START, 0);
  setLong(player, KEY_RUNNING_UNTIL, 0);
}

function isTripRunning(player) {
  return getTripUntilTick(player) > Tripping.nowTick;
}

function formatTripLeft(player) {
  var start = getTripStartTick(player);
  var until = getTripUntilTick(player);

  var totalTicks = until - start;
  var leftTicks  = until - Tripping.nowTick;

  if (totalTicks < 1) totalTicks = 1;
  if (leftTicks < 0) leftTicks = 0;

  var totalSec = Math.ceil(totalTicks / 20);
  var leftSec  = Math.ceil(leftTicks / 20);

  if (totalSec < 1) totalSec = 1;
  if (leftSec < 0) leftSec = 0;

  return leftSec + "s / " + totalSec + "s";
}

function setActiveSubstance(player, itemId) {
  setLong(player, KEY_ACTIVE_SUBSTANCE_HASH, hashId(itemId));
}

function getActiveSubstanceHash(player) {
  return getLong(player, KEY_ACTIVE_SUBSTANCE_HASH);
}

// Effect handling

function collectEffectIds(cfg) {
  var ids = {};
  var stages = cfg.stages || [];
  var i = 0;
  var j = 0;

  for (i = 0; i < stages.length; i++) {
    var st = stages[i];
    if (!st || !st.effects) continue;

    for (j = 0; j < st.effects.length; j++) {
      var e = st.effects[j];
      if (e && e.id) ids[String(e.id)] = true;
    }
  }

  var out = [];
  for (var k in ids) out.push(k);
  return out;
}

function clearEffectsList(player, effectIds) {
  if (!player || !player.server) return;
  if (!effectIds || effectIds.length === 0) return;

  var server = player.server;
  var username = String(player.username);
  var sel = playerSelector(player);

  var i = 0;
  for (i = 0; i < effectIds.length; i++) {
    var id = String(effectIds[i]);

    // API path
    try {
      player.potionEffects.remove(id);
      dlogThrottled("api_remove_" + username + "_" + id, "[TRIP][EFFECT][CLEAR][API] " + username + " id=" + id);
      continue;
    } catch (e1) {}

    // direct command fallback
    var r1 = runSilentChecked(server, "effect clear " + escQuotes(username) + " " + id, "CLEAR");
    if (r1 !== 0) continue;

    // execute-as selector fallback
    runSilentChecked(server, "execute as " + sel + " run effect clear @s " + id, "CLEAR");
  }
}

function giveEffect(player, id, seconds, amp, hideParticles) {
  if (!player || !id) return;

  var s = clampInt(seconds, 1, 1000000, 5);
  var a = clampInt(amp, 0, 255, 0);
  var hide = (hideParticles === true);

  // API path (ticks)
  try {
    var ticks = s * 20;
    var showParticles = !hide;
    player.potionEffects.add(String(id), ticks, a, false, showParticles, true);
    dlogThrottled("api_add_" + player.username + "_" + id, "[TRIP][EFFECT][GIVE][API] " + player.username + " id=" + id + " ticks=" + ticks + " amp=" + a);
    return;
  } catch (e1) {}

  // Command fallback
  var server = player.server;
  if (!server) return;

  var username = String(player.username);
  var sel = playerSelector(player);
  var hideStr = hide ? "true" : "false";

  // try direct first
  var r1 = runSilentChecked(
    server,
    "effect give " + escQuotes(username) + " " + String(id) + " " + s + " " + a + " " + hideStr,
    "GIVE"
  );
  if (r1 !== 0) return;

  // then execute-as
  runSilentChecked(
    server,
    "execute as " + sel + " run effect give @s " + String(id) + " " + s + " " + a + " " + hideStr,
    "GIVE"
  );
}


// Particles

function spawnParticles(player, p) {
  if (!player || !player.server || !p || !p.id) return;

  var server = player.server;
  var sel = playerSelector(player);

  var count = clampInt(p.count, 1, 1000000, 40);

  var spread = (p.spread !== undefined) ? Number(p.spread) : 0.5;
  if (isNaN(spread)) spread = 0.5;

  var speed = (p.speed !== undefined) ? Number(p.speed) : 0.02;
  if (isNaN(speed)) speed = 0.02;

  dlogThrottled("part_" + player.username + "_" + String(p.id), "[TRIP][PART] " + player.username + " id=" + p.id + " count=" + count + " spread=" + spread + " speed=" + speed);

  var cmd =
    "execute at " + sel +
    " run particle " + String(p.id) +
    " ~ ~1 ~ " +
    spread + " " + spread + " " + spread + " " +
    speed + " " + count;

  runSilentChecked(server, cmd, "PART");
}

// Prevent burst overlap
function shouldSpawnParticles(player, tripId, stageIndex, stageStartTick, particlesPeriodTicks, particlesMinGapTicks) {
  var pp = clampInt(particlesPeriodTicks, 1, 1000000, 20);
  var gap = (particlesMinGapTicks !== undefined) ? clampInt(particlesMinGapTicks, 1, 1000000, pp + 4) : (pp + 4);

  var elapsed = Tripping.nowTick - stageStartTick;
  if (elapsed < 0) return false;

  if ((elapsed % pp) !== 0) return false;

  var lastKey = KEY_STAGE_PART_PREFIX + tripId + "_" + stageIndex;
  var last = getLong(player, lastKey);

  if (last > 0 && (Tripping.nowTick - last) < gap) return false;

  setLong(player, lastKey, Tripping.nowTick);
  return true;
}

// Antidotes

function playSoundAtPlayer(player, soundId, volume, pitch) {
  if (!player || !player.server) return;

  var sel = playerSelector(player);
  var v = (volume !== undefined) ? Number(volume) : 1;
  var p = (pitch !== undefined) ? Number(pitch) : 1;
  if (isNaN(v)) v = 1;
  if (isNaN(p)) p = 1;

  runSilent(player.server, "execute as " + sel + " at " + sel + " run playsound " + soundId + " player @s ~ ~ ~ " + v + " " + p);
}

function canAntidoteCancel(player, antidoteItemId) {
  var activeHash = getActiveSubstanceHash(player);
  var cfg = Tripping.substancesByHash[activeHash];
  if (!cfg) return false;

  var list = Tripping.antidoteMap[String(antidoteItemId)];
  if (!list || list.length === 0) return false;

  var i = 0;
  for (i = 0; i < list.length; i++) {
    if (String(list[i]) === String(cfg._id)) return true;
  }
  return false;
}

function cancelTripNow(player) {
  var activeHash = getActiveSubstanceHash(player);
  var cfg = Tripping.substancesByHash[activeHash];

  clearTripWindow(player);
  clearActiveTripId(player);
  removeKey(player, KEY_ACTIVE_SUBSTANCE_HASH);

  if (cfg && cfg._effectIds && cfg._effectIds.length) {
    clearEffectsList(player, cfg._effectIds);
  }

  clearEffectsList(player, [
    "minecraft:nausea",
    "minecraft:night_vision",
    "minecraft:blindness",
    "minecraft:slowness",
    "minecraft:mining_fatigue"
  ]);
}

// Trip lifecycle reset (prevents relog carryover)

function hardResetPlayerTripState(player) {
  if (!player) return;

  var activeHash = getActiveSubstanceHash(player);
  var cfg = Tripping.substancesByHash[activeHash];

  clearTripWindow(player);
  clearActiveTripId(player);
  removeKey(player, KEY_ACTIVE_SUBSTANCE_HASH);

  if (cfg && cfg._effectIds && cfg._effectIds.length) {
    clearEffectsList(player, cfg._effectIds);
  }

  clearEffectsList(player, [
    "minecraft:nausea",
    "minecraft:night_vision",
    "minecraft:blindness",
    "minecraft:slowness",
    "minecraft:mining_fatigue"
  ]);
}

PlayerEvents.loggedIn(function(event) {
  hardResetPlayerTripState(event.player);
});

try {
  PlayerEvents.loggedOut(function(event) {
    hardResetPlayerTripState(event.player);
  });
} catch (e) {}

// Stage runner

function runStage(server, playerUuidStr, username, tripId, stage, stageIndex, stageStartTick, stageEndTick) {
  var effectPeriod = clampInt(stage.periodTicks, 1, 1000000, 10);

  var particlePeriod = clampInt(stage.particlesPeriodTicks, 1, 1000000, 20);
  var particleGap = (stage.particlesMinGapTicks !== undefined)
    ? clampInt(stage.particlesMinGapTicks, 1, 1000000, particlePeriod + 4)
    : (particlePeriod + 4);

  dlog("[TRIP][STAGE] schedule user=" + username + " stage=" + stageIndex + " start=" + stageStartTick + " end=" + stageEndTick + " effPeriod=" + effectPeriod + " partPeriod=" + particlePeriod);

  function stepEffects(tick) {
    if (tick >= stageEndTick) {
      dlog("[TRIP][STAGE] effects done user=" + username + " stage=" + stageIndex + " tick=" + tick);
      return;
    }

    var player = resolvePlayer(server, playerUuidStr, username);
    if (!player) {
      dlogThrottled("no_player_eff_" + username, "[TRIP][STAGE][EFFECTS] resolvePlayer FAILED user=" + username + " uuid=" + playerUuidStr + " tick=" + tick);
      Tripping.scheduleAbs(tick + effectPeriod, function() { stepEffects(tick + effectPeriod); });
      return;
    }

    var cur = getActiveTripId(player);
    if (cur !== tripId) {
      dlogThrottled("token_mismatch_eff_" + username, "[TRIP][STAGE][EFFECTS] token mismatch user=" + username + " have=" + cur + " need=" + tripId + " tick=" + tick);
      return;
    }

    if (stage.message) {
      var msgKey = KEY_STAGE_MSG_PREFIX + tripId + "_" + stageIndex;
      if (!getBool(player, msgKey)) {
        setBool(player, msgKey, true);
        dlog("[TRIP][MSG] user=" + username + " stage=" + stageIndex + " msg=" + stage.message);
        tell(player, String(stage.message), stage.messageColor ? String(stage.messageColor) : "light_purple");
      }
    }

    if (stage.effects) {
      var i = 0;
      for (i = 0; i < stage.effects.length; i++) {
        var e = stage.effects[i];
        if (!e || !e.id) continue;

        dlogThrottled("give_" + username + "_" + stageIndex + "_" + String(e.id),
          "[TRIP][EFFECT] user=" + username + " stage=" + stageIndex + " id=" + e.id +
          " sec=" + ((e.seconds !== undefined) ? e.seconds : 5) +
          " amp=" + ((e.amp !== undefined) ? e.amp : 0) +
          " hide=" + (e.hideParticles === true));

        giveEffect(
          player,
          String(e.id),
          (e.seconds !== undefined) ? e.seconds : 5,
          (e.amp !== undefined) ? e.amp : 0,
          (e.hideParticles === true)
        );
      }
    }

    Tripping.scheduleAbs(tick + effectPeriod, function() { stepEffects(tick + effectPeriod); });
  }

  function stepParticles(tick) {
    if (tick >= stageEndTick) {
      dlog("[TRIP][STAGE] particles done user=" + username + " stage=" + stageIndex + " tick=" + tick);
      return;
    }

    var player = resolvePlayer(server, playerUuidStr, username);
    if (!player) {
      dlogThrottled("no_player_part_" + username, "[TRIP][STAGE][PART] resolvePlayer FAILED user=" + username + " uuid=" + playerUuidStr + " tick=" + tick);
      Tripping.scheduleAbs(tick + 1, function() { stepParticles(tick + 1); });
      return;
    }

    var cur = getActiveTripId(player);
    if (cur !== tripId) {
      dlogThrottled("token_mismatch_part_" + username, "[TRIP][STAGE][PART] token mismatch user=" + username + " have=" + cur + " need=" + tripId + " tick=" + tick);
      return;
    }

    if (stage.particles) {
      if (shouldSpawnParticles(player, tripId, stageIndex, stageStartTick, particlePeriod, particleGap)) {
        dlogThrottled("spawn_part_" + username + "_" + stageIndex, "[TRIP][STAGE][PART] spawn user=" + username + " stage=" + stageIndex + " tick=" + tick);
        spawnParticles(player, stage.particles);
      }
    }

    Tripping.scheduleAbs(tick + 1, function() { stepParticles(tick + 1); });
  }

  Tripping.scheduleAbs(stageStartTick, function() { stepEffects(stageStartTick); });
  if (stage.particles) {
    Tripping.scheduleAbs(stageStartTick, function() { stepParticles(stageStartTick); });
  }
}

// Trip runner

function runTrip(player, cfg) {
  if (!player || !player.server) return;

  if (isTripRunning(player)) {
    tell(player, "You’re already tripping. (" + formatTripLeft(player) + ")", "gray");
    return;
  }

  var tripId = Tripping.nowTick + Math.floor(Math.random() * 1000000);
  setActiveTripId(player, tripId);
  setActiveSubstance(player, cfg._id);

  if (cfg._effectIds && cfg._effectIds.length) {
    clearEffectsList(player, cfg._effectIds);
  }

  var server = player.server;
  var username = String(player.username);
  var playerUuidStr = String(player.uuid);

  var stages = cfg.stages || [];
  if (!stages.length) {
    dlog("[TRIP][RUN] no stages for substance=" + cfg._id);
    return;
  }

  var startBase = Tripping.nowTick;

  var starts = [];
  var i = 0;
  for (i = 0; i < stages.length; i++) {
    var sec = Number(stages[i].atSeconds) || 0;
    starts[i] = startBase + Math.floor(sec * 20);
  }

  var tripEndTick = starts[starts.length - 1] + 200;
  setTripWindow(player, startBase, tripEndTick);

  dlog("[TRIP][RUN] start player=" + username + " uuid=" + playerUuidStr + " substance=" + cfg._id + " tripId=" + tripId + " startTick=" + startBase + " endTick=" + tripEndTick + " now=" + Tripping.nowTick);

  for (i = 0; i < stages.length; i++) {
    dlog("[TRIP][RUN]  stage[" + i + "] atSeconds=" + (Number(stages[i].atSeconds) || 0) + " startTick=" + starts[i]);
  }
  for (i = 0; i < stages.length; i++) {
    var stStart = starts[i];
    var stEnd = (i + 1 < stages.length) ? starts[i + 1] : tripEndTick;
    runStage(server, playerUuidStr, username, tripId, stages[i], i, stStart, stEnd);
  }

  Tripping.scheduleAbs(tripEndTick, function() {
    var p = resolvePlayer(server, playerUuidStr, username);
    if (!p) {
      dlog("[TRIP][END] resolvePlayer failed user=" + username);
      return;
    }
    if (getActiveTripId(p) === tripId) {
      dlog("[TRIP][END] hard reset user=" + username + " tripId=" + tripId);
      hardResetPlayerTripState(p);
    }
  });
}

// Item hooks

ItemEvents.rightClicked(function(event) {
  var player = event.player;
  var item = event.item;

  if (!player || !player.server) return;
  if (!item || item.isEmpty()) return;

  var id = String(item.id);

  dlog("[TRIP][CLICK] player=" + player.username + " item=" + id + " now=" + Tripping.nowTick);

  // Antidote use
  var anti = Tripping.antidotes[id];
  if (anti) {
    if (!isTripRunning(player)) {
      tell(
        player,
        anti.noTripMessage ? String(anti.noTripMessage) : "You’re not tripping.",
        anti.noTripMessageColor || "gray"
      );
      event.cancel();
      return;
    }

    if (!canAntidoteCancel(player, id)) {
      tell(
        player,
        anti.wrongTripMessage ? String(anti.wrongTripMessage) : "That antidote doesn’t work for this trip.",
        anti.wrongTripMessageColor || "gray"
      );
      event.cancel();
      return;
    }

    var antiAmt = clampInt(anti.consumeAmount, 1, 64, 1);
    if (anti.consumeOnUse !== false) item.shrink(antiAmt);

    if (anti.soundId) playSoundAtPlayer(player, String(anti.soundId), anti.soundVolume, anti.soundPitch);
    spawnParticles(player, anti.particles || { id: "minecraft:instant_effect", count: 20, spread: 0.2, speed: 0.02 });

    cancelTripNow(player);

    tell(
      player,
      anti.cancelMessage ? String(anti.cancelMessage) : "Antidote administered — your head clears.",
      anti.cancelMessageColor || "aqua"
    );

    event.cancel();
    return;
  }

  // Substance use
  var cfg = Tripping.substances[id];
  if (!cfg) return;

  if (isTripRunning(player)) {
    tell(player, "You’re already tripping. (" + formatTripLeft(player) + ")", "gray");
    event.cancel();
    return;
  }

  var amt = clampInt(cfg.consumeAmount, 1, 64, 1);
  if (cfg.consumeOnUse !== false) item.shrink(amt);

  runTrip(player, cfg);

  event.cancel();
});

// Mapping: antidote item id -> list of substances it can cancel

Tripping.antidoteMap = {
  "kubejs:kappa_reset_ampoule": ["gtceu:salvinorin_a_dust"]
};

// Substances

Tripping.register("gtceu:salvinorin_a_dust", {
  consumeOnUse: true,
  consumeAmount: 1,

  stages: [
    {
      atSeconds: 0,
      periodTicks: 10,
      particlesPeriodTicks: 20,
      particlesMinGapTicks: 26,

      message: "Come-up… the world starts bending.",
      messageColor: "light_purple",

      effects: [
        { id: "minecraft:nausea", seconds: 12, amp: 0, hideParticles: true },
        { id: "minecraft:night_vision", seconds: 14, amp: 0, hideParticles: true }
      ],

      particles: { id: "minecraft:portal", count: 90, spread: 0.7, speed: 0.05 }
    },

    {
      atSeconds: 6,
      periodTicks: 10,
      particlesPeriodTicks: 15,
      particlesMinGapTicks: 20,

      message: "Peak.",
      messageColor: "dark_purple",

      effects: [
        { id: "minecraft:nausea", seconds: 18, amp: 1, hideParticles: true },
        { id: "minecraft:blindness", seconds: 3, amp: 0, hideParticles: true },
        { id: "minecraft:slowness", seconds: 10, amp: 0, hideParticles: true }
      ],

      particles: { id: "minecraft:enchant", count: 140, spread: 0.9, speed: 0.02 }
    },

    {
      atSeconds: 20,
      periodTicks: 10,
      particlesPeriodTicks: 25,
      particlesMinGapTicks: 30,

      message: "Plateau… colours feel unreal.",
      messageColor: "aqua",

      effects: [
        { id: "minecraft:nausea", seconds: 20, amp: 0, hideParticles: true },
        { id: "minecraft:night_vision", seconds: 25, amp: 0, hideParticles: true },
        { id: "minecraft:mining_fatigue", seconds: 12, amp: 0, hideParticles: true }
      ],

      particles: { id: "minecraft:end_rod", count: 90, spread: 0.8, speed: 0.01 }
    },

    {
      atSeconds: 45,
      periodTicks: 10,
      particlesPeriodTicks: 30,
      particlesMinGapTicks: 36,

      message: "Comedown… fading back to normal.",
      messageColor: "gray",

      effects: [
        { id: "minecraft:nausea", seconds: 10, amp: 0, hideParticles: true }
      ],

      particles: { id: "minecraft:happy_villager", count: 60, spread: 0.7, speed: 0.02 }
    }
  ]
});

// Antidotes

Tripping.registerAntidote("kubejs:kappa_reset_ampoule", {
  consumeOnUse: true,
  consumeAmount: 1,

  cancelMessage: "Antidote administered — your head clears.",
  cancelMessageColor: "aqua",

  noTripMessage: "You’re not tripping.",
  noTripMessageColor: "gray",

  wrongTripMessage: "That antidote doesn’t work for this trip.",
  wrongTripMessageColor: "gray",

  soundId: "kubejs:antidote_inject",
  soundVolume: 1,
  soundPitch: 1,

  particles: { id: "minecraft:instant_effect", count: 20, spread: 0.2, speed: 0.02 }
});