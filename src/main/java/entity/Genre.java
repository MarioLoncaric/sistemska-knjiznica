package entity;

import java.io.Serializable;


public record Genre(String name) implements Serializable {

    public Genre {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Zanr mora biti upisan");
        }
    }



    @Override
    public String toString() {
        return name;
    }
}

