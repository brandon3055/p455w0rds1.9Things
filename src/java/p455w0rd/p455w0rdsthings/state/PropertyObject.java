package p455w0rd.p455w0rdsthings.state;

import java.util.Objects;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyObject<T> implements IUnlistedProperty<T> {

	private final String name;
	private final Class<T> clazz;
	private final Predicate<T> validator;
	private final Function<T, String> stringFunction;

	public PropertyObject(String name, Class<T> clazz, Predicate<T> validator, Function<T, String> stringFunction) {
		this.name = name;
		this.clazz = clazz;
		this.validator = validator;
		this.stringFunction = stringFunction;
	}

	public PropertyObject(String name, Class<T> clazz) {
		this(name, clazz, Predicates.<T>alwaysTrue(), new Function<T, String>() {
			@Override
			public String apply(T input) {
				return Objects.toString(input);
			}
		});
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isValid(T value) {
		return validator.apply(value);
	}

	@Override
	public Class<T> getType() {
		return clazz;
	}

	@Override
	public String valueToString(T value) {
		return stringFunction.apply(value);
	}
}