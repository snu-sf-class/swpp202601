interface UserI {
    public void print();
}
interface UserAdvancedI {
    public void print_header();
    public void print_with_header(f: void -> void);
}

class User implements UserI, UserAdvancedI {
    private String name;
    private String address;

    public User(String name, String address) {
	this.name = name;
	this.address = address;
    }

    public void print_header() {
	System.out.println("I am a User.");
    }
    
    public void print_with_header(header_print_f: void -> void) {
	header_print_f();
	System.out.println(name);
	System.out.println(address);
    }
    public void print() {
	print_with_hdeader(print_header);
    }
}

class VIP implements UserI {
    private UserI user;
    private String account;

    public VIP(String name, String address, String account) {
	user = new User(name, address);
	this.account = account;
    }
    
    public void print() {
	user.print();
	System.out.println(account);
    }
}

class Main {
    static void printUser(UserI user) {
	user.print();
    }

    
    public static void main(String[ ] args) {
	User user = new User("sunghwan","SNU");
	VIP vip = new VIP("gil", "SNU", "1234");

	printUser(user);
	printUser(vip);
    }
}















