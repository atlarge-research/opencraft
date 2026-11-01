package science.atlarge.opencraft.opencraft.constants;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link GlowPotionEffect#pulse}.
 */
public class PotionEffectPulseTest {

    private LivingEntity entity;

    @BeforeAll
    public static void staticSetup() {
        GlowPotionEffect.register();
    }

    @BeforeEach
    public void before() {
        entity = mock(LivingEntity.class);
        AttributeInstance maxHealth = mock(AttributeInstance.class);
        when(maxHealth.getValue()).thenReturn(20.0);
        when(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(maxHealth);
        when(entity.getHealth()).thenReturn(10.0);
    }

    private void pulse(PotionEffectType type, int amplifier, int ticksLeft) {
        GlowPotionEffect.getEffect(type)
                .pulse(entity, new PotionEffect(type, ticksLeft, amplifier));
    }

    @Test
    public void testRegenerationHealsEveryFiftyTicks() {
        pulse(PotionEffectType.REGENERATION, 0, 100);
        verify(entity).setHealth(11.0);
    }

    @Test
    public void testRegenerationDoesNothingBetweenIntervals() {
        pulse(PotionEffectType.REGENERATION, 0, 99);
        verify(entity, never()).setHealth(anyDouble());
    }

    @Test
    public void testRegenerationIntervalHalvesPerAmplifier() {
        pulse(PotionEffectType.REGENERATION, 1, 25);
        verify(entity).setHealth(11.0);
    }

    @Test
    public void testPoisonDamagesEveryTwentyFiveTicks() {
        pulse(PotionEffectType.POISON, 0, 50);
        verify(entity).damage(1, DamageCause.POISON);
    }

    @Test
    public void testPoisonDoesNotKill() {
        when(entity.getHealth()).thenReturn(1.0);
        pulse(PotionEffectType.POISON, 0, 50);
        verify(entity, never()).damage(anyDouble(), any(DamageCause.class));
    }

    @Test
    public void testWitherDamagesEveryFortyTicks() {
        pulse(PotionEffectType.WITHER, 0, 80);
        verify(entity).damage(1, DamageCause.WITHER);
    }

    @Test
    public void testWitherCanKill() {
        when(entity.getHealth()).thenReturn(1.0);
        pulse(PotionEffectType.WITHER, 0, 40);
        verify(entity).damage(1, DamageCause.WITHER);
    }
}
