package interfaces;

import model.Offer;

import java.io.IOException;
import java.util.Set;

public interface Parser {
    void parse(String url, Set<Offer> offers) throws IOException;
}
