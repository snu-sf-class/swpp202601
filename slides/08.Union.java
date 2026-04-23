A+B

inl: A -> A+B 
inr: B -> A+B

match (x: A+B) with
| a:A => a.op1
| b:B => b.op3
end


class A {
  int x;
  float y;
  int foo(int x);
}

A + B <= bool * A * B

A * B = {(a,b) | a \in A, b \in B}
A + B = {inl a | a \in A} + {inr b | b \in B} 

*
+
    
A\subset X, B \subset X
    
(A \cap B)  \subset X
(A \cub B)  \subset X

A \cap B =   A

and
or    
----------------------

class A { ... }

class B extends A { ... }

class C extends A { override ...}

------------------------------------

interface Tree {...}.
    
class Leaf implements Tree {
    ...
}
class Node implements Tree {
   chl : list Tree

   chl.get(0)....       
}

      Tree 
    /     \
   Node  Leaf

    Node -> Tree, Tree

--------------------------------
interface A { op0; op1; op2 }
interface B { op0; op3 }

interface AorB extends A, B {
    Boolean isA();
}
--------------------------------
interface AandB extends A, B {
}

class AUB {
    Boolean isA() { throw ...; }
    op0() { throw ...; }
    op1() { throw ...; }
    op2() { throw ...; }
    op3() { throw ...; }
}

class A extends AUB {
    isA() { return true; }
    op0() { ... };
    op1() { ... };
    op2() { ... };
}

class B extends AUB {
    isA() { return false; }
    op0() { ... };
    op3() { ... };
}

----------------------
interface AUB {
    Boolean isA();
    op0();
    op1();
    op2();
    op3();
}

class A implements AUB {
    isA() { return true; }
    op0() { ... };
    op1() { ... };
    op2() { ... };
    op3() { throw ... };
}

class B implements AUB {
    isA() { return false; }
    op0() { ... };
    op1() { throw ... };
    op2() { throw ... };
    op3() { ... };
}

-------------------------

What is the problem with the above approaches?   

f(AUB u) {
    u.op0();
    u.op1();  // may trigger exception
}

f(AUB u) {
    u.op0();
    // but has to know op1, op2 are available when isA(); also op3 when not isA().
    if (u.isA()) { u.op1(); u.op2(); } 
    else { u.op3(); }
}

-----------------------
interface AB { op0; }
interface A extends AB { op1; op2 }
interface B extends AB { op3 }

interface AUB extends AB {
    A getA();
    B getB();
}

class Aimpl implements A, AUB {
    A getA() { return this; }
    B getB() { return null; }
    op0() { ... };
    op1() { ... };
    op2() { ... };
}

class Bimpl implements B, AUB {
    AI getA() { return null; }
    BI getB() { return this; }
    op0() { ... };
    op3() { ... };
}

f(AUB u) {
    A a;
    B b;
    u.op0();
    if ((a = u.getA()) != null) {
	a.op1(); a.op2();
    } else if ((b = u.getB()) != null) {
	b.op0(); b.op3();
    } else { throw ...; } // impossible case
}

----------------------------------

interface AB { op0; }
interface A extends AB { op1; op2 }
interface B extends AB { op3 }

interface AorB extends AB {
    R match[R](fa: A => R, fb: B => R);
}

f(AorB u) {
    u.op0();
    u.match(
      ((a:A) a.op1(); a.op2();),
      ((b:B) b.op0(); b.op3();));
}

--------------

class AImpl implements A {
    public void op0() { System.out.println("A.op0"); }
    public void op1() { System.out.println("A.op1"); }
    public void op2() { System.out.println("A.op2"); }
}

class BImpl implements B {
    public void op0() { System.out.println("B.op0"); }
    public void op3() { System.out.println("B.op3"); }
}

public class Demo {
    public static void main(String[] args) {
        AorB x = new AorBImpl(new AImpl());
        AorB y = new AorBImpl(new BImpl());

        String rx = x.match(
            a -> { a.op1(); return "got A"; },
            b -> { b.op3(); return "got B"; }
        );

        String ry = y.match(
            a -> "A branch",
            b -> "B branch"
        );

        System.out.println(rx); // "got A"
        System.out.println(ry); // "B branch"
    }
}    
