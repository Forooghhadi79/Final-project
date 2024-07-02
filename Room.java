public record Room(String name) {


    @Override
    public String toString() {
        return name;
    }

    @Override
    public String name() {
        return "name";
    }
}


