package com.majruszsaccessories.items;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.list.EnhancePotions;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.contexts.OnLootContext;
import net.minecraft.world.entity.monster.Witch;

import java.util.function.Supplier;

import static com.majruszsaccessories.MajruszsAccessories.CONFIG_HANDLER;

public class SecretIngredientItem extends AccessoryItem {
	static final String ID = Registries.getLocationString( "secret_ingredient" );
	static final ConfigGroup GROUP = CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( ID, "SecretIngredient", "" ) );

	public static Supplier< SecretIngredientItem > create() {
		GameModifiersHolder< SecretIngredientItem > holder = new GameModifiersHolder<>( ID, SecretIngredientItem::new );
		holder.addModifier( EnhancePotions::new );
		holder.addModifier( AddDropChance::new );

		return holder::getRegistry;
	}

	static class AddDropChance extends AccessoryModifier {
		public AddDropChance( Supplier< ? extends AccessoryItem > item, String configKey ) {
			super( item, configKey, "", "" );

			OnLootContext onLoot = new OnLootContext( this::addToGeneratedLoot );
			onLoot.addCondition( new Condition.IsServer() )
				.addCondition( new Condition.Chance( 0.01, "drop_chance", "Chance for Secret Ingredient to drop from Witch." ) )
				.addCondition( OnLootContext.HAS_LAST_DAMAGE_PLAYER )
				.addCondition( data->data.entity instanceof Witch );

			this.addContext( onLoot );
		}
	}
}