package com.majruszsaccessories.items;

import com.majruszsaccessories.AccessoryHandler;
import com.mlib.gamemodifiers.GameModifiersHolder;
import com.mlib.gamemodifiers.IRegistrable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AccessoryItem extends Item implements IRegistrable {
	public static final List< GameModifiersHolder< ? extends AccessoryItem > > ACCESSORIES = new ArrayList<>();
	GameModifiersHolder< ? > holder = null;

	public AccessoryItem() {
		super( new Properties().stacksTo( 1 ) );
	}

	@Override
	public void setHolder( GameModifiersHolder< ? > holder ) {
		this.holder = holder;
	}

	@Override
	public GameModifiersHolder< ? > getHolder() {
		return this.holder;
	}

	@Override
	public void onCraftedBy( ItemStack itemStack, Level level, Player player ) {
		AccessoryHandler handler = new AccessoryHandler( itemStack );
		if( handler.hasBonusRangeTag() ) {
			handler.applyBonusRange();
		}
	}

	@Override
	public boolean isFoil( ItemStack itemStack ) {
		return new AccessoryHandler( itemStack ).hasMaxBonus();
	}

	@Override
	public Rarity getRarity( ItemStack itemStack ) {
		return new AccessoryHandler( itemStack ).getItemRarity();
	}

	protected static < Type extends AccessoryItem > GameModifiersHolder< Type > newHolder( String configKey,
		Supplier< Type > supplier
	) {
		GameModifiersHolder< Type > holder = new GameModifiersHolder<>( configKey, supplier );
		ACCESSORIES.add( holder );

		return holder;
	}
}