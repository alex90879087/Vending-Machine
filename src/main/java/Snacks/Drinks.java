package Snacks;

public class Drinks implements Snacks{
    private String name;
    private double price;
    private int quantity;
    private String code;
    private int sold;

    public Drinks(String name, String code, double price) {
        this.name = name;
        this.price = price;
        this.quantity = 7;
        this.code = code;
        this.sold = 0;
    }

    public Drinks(String name, String code, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.code = code;
        this.sold = 0;

    }


    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public void sold(int qty) {
        this.sold += qty;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public double getPrice() {
        return this.price;
    }

    @Override
    public int getSold() {
        return this.sold;
    }


//    public static class ComputerBuilder {
//        private Part cpu;
//        private Part motherboard;
//        private List<Part> hdds;
//        private List<Part> ram;
//        private Part enclosure;
//
//        public ComputerBuilder() {
//            // Default values
//            cpu = new Part();
//            motherboard = new Part();
//            hdds = new ArrayList<>(List.of(new Part()));
//            ram = new ArrayList<>(List.of(new Part()));
//            enclosure = new Part();
//        }
//
//        /*
//        This sort of thing is also common to see; returning
//        the builder itself so it is really easy to just string
//        together method calls, e.g.
//
//        Computer.ComputerBuilder cb = new Computer.ComputerBuilder();
//        Computer c = cb.setCPU(cpu).setMotherboard(motherboard).addHDD(hdd).build();
//        */
//        public ComputerBuilder setCPU(Part cpu) {
//            this.cpu = cpu;
//            return this;
//        }
//
//        // ...and the others...
//
//        public Computer build() {
//            return new Computer(cpu, motherboard, hdds, ram, enclosure);
//        }
//    }


}
