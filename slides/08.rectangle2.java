class Adaptor extends Rectanlge, NonScalableRectangle {
    Rectanlge(NonScalableRectangle r) {
	rect = r;
    }
    override void setScale(float factor) {
	rect.width(rect.getWidth() * factor);
	rect.height(rect.getHeight() * factor);
    }
    float getArea() { return rect.getArea(); }
}


r = new NonScalableRectangle(nr);
f(r);

