#version 330 core

out vec4 FragColor;
in vec2 TexCoords;

uniform sampler2D screenTexture;
uniform vec2 gdwResolution;
uniform float gdwAge;
uniform float gdwProgress;
uniform float gdwIntensity;
uniform float gdwPrism;
uniform float gdwTunnel;
uniform float gdwRibbon;
uniform float gdwWobble;

const float TAU = 6.28318530718;

float saturate(float value) {
    return clamp(value, 0.0, 1.0);
}

vec2 clampUv(vec2 uv) {
    return clamp(uv, vec2(0.001), vec2(0.999));
}

void main() {
    vec4 base = texture(screenTexture, TexCoords);
    if (gdwIntensity <= 0.0001) {
        FragColor = base;
        return;
    }

    float aspect = gdwResolution.x / max(1.0, gdwResolution.y);
    float accel = 1.0 + (gdwProgress * 3.75);
    float lsdStrength = saturate(gdwIntensity);

    vec2 uv = TexCoords;
    vec2 centered = uv * 2.0 - 1.0;
    centered.x *= aspect;

    float radius = length(centered);
    float angle = atan(centered.y, centered.x);

    float swirl = sin(radius * 14.0 - gdwAge * 0.13 * accel + sin(angle * 6.0 + gdwAge * 0.05)) * 0.06;
    angle += swirl * gdwTunnel * lsdStrength;

    float sectorCount = max(5.0, 5.0 + floor((gdwRibbon * 10.0) + (gdwPrism * 8.0)));
    float sector = TAU / sectorCount;
    float mirroredAngle = abs(mod(angle + sector * 0.5, sector) - sector * 0.5);
    angle = mix(angle, mirroredAngle, saturate(lsdStrength * (0.30 + gdwPrism * 0.75)));

    radius += sin(gdwAge * 0.055 * accel + radius * 28.0) * (0.08 * gdwTunnel * lsdStrength);
    radius += cos(gdwAge * 0.041 * accel + angle * 11.0) * (0.03 * gdwRibbon * lsdStrength);

    vec2 warped = vec2(cos(angle), sin(angle)) * radius;
    warped.x /= aspect;
    vec2 distortedUv = warped * 0.5 + 0.5;

    vec2 wave = vec2(
        sin((uv.y * (17.0 + gdwRibbon * 44.0)) + (gdwAge * 0.06 * accel) + sin(uv.x * 9.0 + gdwAge * 0.03)) * (0.018 + gdwWobble * 0.040),
        cos((uv.x * (15.0 + gdwPrism * 48.0)) - (gdwAge * 0.05 * accel) + cos(uv.y * 13.0 - gdwAge * 0.04)) * (0.014 + gdwRibbon * 0.032)
    ) * lsdStrength;
    distortedUv += wave;

    float breathe = 1.0 + sin(gdwAge * 0.040 * accel) * (0.045 * gdwTunnel * lsdStrength);
    distortedUv = ((distortedUv - 0.5) / breathe) + 0.5;

    vec2 chromaDir = normalize(vec2(cos(gdwAge * 0.11), sin(gdwAge * 0.09)) + vec2(0.001));
    vec2 chromaOffset = chromaDir * (0.002 + gdwPrism * 0.030) * lsdStrength;

    vec2 echoOffset = vec2(
        sin(gdwAge * 0.025 * accel + radius * 9.0),
        cos(gdwAge * 0.021 * accel - angle * 7.0)
    ) * (0.004 + gdwWobble * 0.020) * lsdStrength;

    vec2 finalUv = clampUv(distortedUv);
    vec2 uvR = clampUv(finalUv + chromaOffset);
    vec2 uvG = clampUv(finalUv + echoOffset * 0.35);
    vec2 uvB = clampUv(finalUv - chromaOffset);
    vec2 uvEcho = clampUv(finalUv - echoOffset);

    vec4 rSample = texture(screenTexture, uvR);
    vec4 gSample = texture(screenTexture, uvG);
    vec4 bSample = texture(screenTexture, uvB);
    vec4 echoSample = texture(screenTexture, uvEcho);

    vec3 color = vec3(rSample.r, gSample.g, bSample.b);
    color = mix(color, echoSample.rgb, saturate(0.10 + gdwTunnel * 0.18) * lsdStrength);

    float veil = (0.05 + gdwRibbon * 0.10) * lsdStrength
        * (0.5 + 0.5 * sin(gdwAge * 0.08 * accel + radius * 16.0 + angle * 3.0));
    vec3 veilColor = vec3(
        0.5 + 0.5 * sin(gdwAge * 0.13 + angle * 3.0),
        0.5 + 0.5 * sin(gdwAge * 0.17 + angle * 5.0 + 2.094),
        0.5 + 0.5 * sin(gdwAge * 0.19 + angle * 7.0 + 4.188)
    );

    color += veil * veilColor;
    color = mix(base.rgb, color, saturate(0.35 + lsdStrength * 0.95));

    FragColor = vec4(saturate(color.r), saturate(color.g), saturate(color.b), base.a);
}
