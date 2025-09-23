package guru.qa.niffler.utils.converter;

import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BrowserConverter implements ArgumentConverter {

  @Nonnull
  @Override
  @SneakyThrows
  public Object convert(@Nonnull Object source, ParameterContext context) throws ArgumentConversionException {
    Class<?> target = context.getParameter().getType();
    if (!Browser.class.equals(target)) {
      throw new ArgumentConversionException("Waiting Browser.class type, given: " + target);
    }
    return Browser.valueOf(source.toString());
  }
}
