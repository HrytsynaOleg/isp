package price;

import enums.FileFormat;
import price.impl.DocxPriceBuilder;
import price.impl.PdfPriceBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class PriceFactory {
    private static final List<IPriceBuilder> pricesList= new ArrayList<>();
    private static PriceFactory instance;

    private PriceFactory() {
        pricesList.add(new PdfPriceBuilder());
        pricesList.add(new DocxPriceBuilder());
    }

    public IPriceBuilder getPriceBuilder (FileFormat type) {

        return pricesList.stream()
                .filter(a->a.getPriceFormat()==type)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public static PriceFactory getInstance() {
        if (instance == null) {
            instance = new PriceFactory();
        }
        return instance;


    }
}
