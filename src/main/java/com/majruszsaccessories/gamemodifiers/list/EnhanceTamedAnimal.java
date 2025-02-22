package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.attributes.AttributeHandler;
import com.mlib.effects.ParticleHandler;
import com.mlib.gamemodifiers.contexts.OnAnimalTame;
import com.mlib.math.Range;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;

import java.util.function.Supplier;

public class EnhanceTamedAnimal extends AccessoryModifier {
	static final AttributeHandler HEALTH = new AttributeHandler( "ec10e191-9ab4-40e5-a757-91e57104d3ab", "CoTHealthMultiplier", Attributes.MAX_HEALTH, AttributeModifier.Operation.MULTIPLY_BASE );
	static final AttributeHandler DAMAGE = new AttributeHandler( "f1d3671c-9474-4ffd-a440-902d69bd3bd9", "CoTDamageMultiplier", Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE );
	static final AttributeHandler SPEED = new AttributeHandler( "ed1c5feb-1017-4dc2-8a8b-7a64388f0dea", "CoTSpeedMultiplier", Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE );
	static final AttributeHandler JUMP_HEIGHT = new AttributeHandler( "8c065a4c-98de-4ce4-adda-41ae7a20abfb", "CoTJumpHeightMultiplier", Attributes.JUMP_STRENGTH, AttributeModifier.Operation.MULTIPLY_BASE );
	final AccessoryPercent bonus;

	public EnhanceTamedAnimal( Supplier< ? extends AccessoryItem > item, String configKey ) {
		this( item, configKey, 0.2 );
	}

	public EnhanceTamedAnimal( Supplier< ? extends AccessoryItem > item, String configKey, double bonus ) {
		super( item, configKey );

		this.bonus = new AccessoryPercent( bonus, new Range<>( 0.0, 10.0 ) );

		new OnAnimalTame.Context( this.toAccessoryConsumer( this::enhanceAnimal ) )
			.addConfig( this.bonus.name( "animal_bonus" ).comment( "Bonus health, damage, movement speed and jump height for tamed animals." ) )
			.insertTo( this );

		this.addTooltip( this.bonus, "majruszsaccessories.bonuses.animal_health" );
		this.addTooltip( this.bonus, "majruszsaccessories.bonuses.animal_damage" );
		this.addTooltip( this.bonus, "majruszsaccessories.bonuses.animal_speed" );
		this.addTooltip( this.bonus, "majruszsaccessories.bonuses.animal_jump_height" );
	}

	private void enhanceAnimal( OnAnimalTame.Data data, AccessoryHandler handler ) {
		float bonus = this.bonus.getValue( handler );
		HEALTH.setValueAndApply( data.animal, bonus );
		if( AttributeHandler.hasAttribute( data.animal, Attributes.ATTACK_DAMAGE ) ) {
			DAMAGE.setValueAndApply( data.animal, bonus );
		}
		if( data.animal instanceof Horse horse ) {
			JUMP_HEIGHT.setValueAndApply( horse, bonus );
			SPEED.setValueAndApply( horse, bonus );
		}
		data.animal.setHealth( data.animal.getMaxHealth() );
		if( data.level != null ) {
			ParticleHandler.AWARD.spawn( data.level, data.animal.position().add( 0.0, 0.5, 0.0 ), 8, ParticleHandler.offset( 2.0f ) );
		}
	}
}
