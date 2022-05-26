package transformer;

public class GResizerDto {
	private double tx;
	private double ty;
	private double sx;
	private double sy;

	public GResizerDto(double tx, double ty, double sx, double sy) {
		this.tx = tx;
		this.ty = ty;
		this.sx = sx;
		this.sy = sy;
	}

	public double getTx() {
		return tx;
	}

	public void setTx(double tx) {
		this.tx = tx;
	}

	public double getTy() {
		return ty;
	}

	public void setTy(double ty) {
		this.ty = ty;
	}

	public double getSx() {
		return sx;
	}

	public void setSx(double sx) {
		this.sx = sx;
	}

	public double getSy() {
		return sy;
	}

	public void setSy(double sy) {
		this.sy = sy;
	}
}
