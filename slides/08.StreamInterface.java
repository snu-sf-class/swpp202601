interface InputStream {
    char read();
}
class CommonInputStream {
    ....
}
class FileInputStream implements InputStream {
    CommonInputStream str ;
    FileInputStream(CommonInputStream c) {
	str = c;
    }
    char read() {
	... str ...
    }
}
class ConsleInputStream implements InputStream {
    ConsleInputStream(ConsleInputStream c)  {
	str = c;
    }
    char read() {
	... str ... 
    }
}

class LowerCaseTopping implements InputStream {
    InputStream str;
    LowerCaseTopping(InputStream s) {
	str = s;
    }
    char read() {
	char c = str.read();
	return c.to_lower();
    }
}

class BufferedTopping implements InputStream {
    InputStream str;
    LowerCaseTopping(InputStream s) {
	str = s;
    }
    char read() {
	...
    }
}

new LowerCaseTopping (new BufferedTopping (new BufferedTopping(new FileInputStream())))

    
