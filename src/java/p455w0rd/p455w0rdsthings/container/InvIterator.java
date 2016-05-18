package p455w0rd.p455w0rdsthings.container;

import java.util.Iterator;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public final class InvIterator implements Iterator<ItemStack>
{
	private final IInventory inventory;
	private final int size;

	private int counter = 0;

	public InvIterator( final IInventory inventory )
	{
		this.inventory = inventory;
		this.size = this.inventory.getSizeInventory();
	}

	@Override
	public boolean hasNext()
	{
		return this.counter < this.size;
	}

	@Override
	public ItemStack next()
	{
		final ItemStack result = this.inventory.getStackInSlot( this.counter );
		this.counter++;

		return result;
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}
}
