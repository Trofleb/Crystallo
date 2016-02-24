import java.lang.reflect.Method;

public class CellGen {
	public static float[][] pos1(float x, float y, float z) {
		return new float[][] { { x, y, z }, };
	}

	public static float[][] pos2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, -z }, };
	}

	public static float[][] pos3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, y, -z }, };
	}

	public static float[][] pos3alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, };
	}

	public static float[][] pos4(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, y + 1f / 2f, -z }, };
	}

	public static float[][] pos4alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, };
	}

	public static float[][] pos5(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, y, -z }, { x + 1f / 2f, y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, -z }, };
	}

	public static float[][] pos5_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, y, -z }, { x, y + 1f / 2f, z + 1f / 2f },
				{ -x, y + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos5_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, y, -z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos5alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x, y + 1f / 2f, z + 1f / 2f },
				{ -x, -y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos5alt_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x + 1f / 2f, y, z + 1f / 2f },
				{ -x + 1f / 2f, -y, z + 1f / 2f }, };
	}

	public static float[][] pos5alt_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos6(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x, -y, z }, };
	}

	public static float[][] pos6alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x, y, -z }, };
	}

	public static float[][] pos7(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x, -y, z + 1f / 2f }, };
	}

	public static float[][] pos7_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x + 1f / 2f, -y, z + 1f / 2f }, };
	}

	public static float[][] pos7_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x + 1f / 2f, -y, z }, };
	}

	public static float[][] pos7alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x + 1f / 2f, y, -z }, };
	}

	public static float[][] pos7alt_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x + 1f / 2f, y + 1f / 2f, -z }, };
	}

	public static float[][] pos7alt_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x, y + 1f / 2f, -z }, };
	}

	public static float[][] pos8(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x, -y, z }, { x + 1f / 2f, y + 1f / 2f, z },
				{ x + 1f / 2f, -y + 1f / 2f, z }, };
	}

	public static float[][] pos8_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x, -y, z }, { x, y + 1f / 2f, z + 1f / 2f },
				{ x, -y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos8_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x, -y, z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos8alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x, y, -z }, { x, y + 1f / 2f, z + 1f / 2f },
				{ x, y + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos8alt_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x, y, -z }, { x + 1f / 2f, y, z + 1f / 2f },
				{ x + 1f / 2f, y, -z + 1f / 2f }, };
	}

	public static float[][] pos8alt_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x, y, -z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos9(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x, -y, z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, z },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos9_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x + 1f / 2f, -y, z + 1f / 2f }, { x, y + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z }, };
	}

	public static float[][] pos9_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x + 1f / 2f, -y, z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ x, -y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos9alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x + 1f / 2f, y, -z }, { x, y + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos9alt_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, y, z + 1f / 2f },
				{ x, y + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos9alt_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { x, y + 1f / 2f, -z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, y, -z + 1f / 2f }, };
	}

	public static float[][] pos10(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, y, -z }, { -x, -y, -z }, { x, -y, z }, };
	}

	public static float[][] pos10alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, -y, -z }, { x, y, -z }, };
	}

	public static float[][] pos11(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, y + 1f / 2f, -z }, { -x, -y, -z }, { x, -y + 1f / 2f, z }, };
	}

	public static float[][] pos11alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, { -x, -y, -z }, { x, y, -z + 1f / 2f }, };
	}

	public static float[][] pos12(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, y, -z }, { -x, -y, -z }, { x, -y, z },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z }, { -x + 1f / 2f, -y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, z }, };
	}

	public static float[][] pos12_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, y, -z }, { -x, -y, -z }, { x, -y, z },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f }, { -x, -y + 1f / 2f, -z + 1f / 2f },
				{ x, -y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos12_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, y, -z }, { -x, -y, -z }, { x, -y, z },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos12alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, -y, -z }, { x, y, -z },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, -z + 1f / 2f },
				{ x, y + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos12alt_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, -y, -z }, { x, y, -z },
				{ x + 1f / 2f, y, z + 1f / 2f }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x + 1f / 2f, -y, -z + 1f / 2f },
				{ x + 1f / 2f, y, -z + 1f / 2f }, };
	}

	public static float[][] pos12alt_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, -y, -z }, { x, y, -z },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos13(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, y, -z + 1f / 2f }, { -x, -y, -z }, { x, -y, z + 1f / 2f }, };
	}

	public static float[][] pos13_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, y, -z + 1f / 2f }, { -x, -y, -z },
				{ x + 1f / 2f, -y, z + 1f / 2f }, };
	}

	public static float[][] pos13_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, y, -z }, { -x, -y, -z }, { x + 1f / 2f, -y, z }, };
	}

	public static float[][] pos13alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z }, { -x, -y, -z }, { x + 1f / 2f, y, -z }, };
	}

	public static float[][] pos13alt_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x, -y, -z },
				{ x + 1f / 2f, y + 1f / 2f, -z }, };
	}

	public static float[][] pos13alt_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y + 1f / 2f, z }, { -x, -y, -z }, { x, y + 1f / 2f, -z }, };
	}

	public static float[][] pos14(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, y + 1f / 2f, -z + 1f / 2f }, { -x, -y, -z },
				{ x, -y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos14_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { -x, -y, -z },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos14_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, y + 1f / 2f, -z }, { -x, -y, -z },
				{ x + 1f / 2f, -y + 1f / 2f, z }, };
	}

	public static float[][] pos14alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x, -y, -z },
				{ x + 1f / 2f, y, -z + 1f / 2f }, };
	}

	public static float[][] pos14alt_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x, -y, -z },
				{ x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos14alt_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y + 1f / 2f, z + 1f / 2f }, { -x, -y, -z },
				{ x, y + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos15(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, y, -z + 1f / 2f }, { -x, -y, -z }, { x, -y, z + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos15_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, y, -z + 1f / 2f }, { -x, -y, -z },
				{ x + 1f / 2f, -y, z + 1f / 2f }, { x, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ -x, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, z }, };
	}

	public static float[][] pos15_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, y, -z }, { -x, -y, -z }, { x + 1f / 2f, -y, z },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x, -y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos15alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z }, { -x, -y, -z }, { x + 1f / 2f, y, -z },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos15alt_2(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x, -y, -z },
				{ x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, y, z + 1f / 2f }, { -x, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y, -z + 1f / 2f }, { x, y + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos15alt_3(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y + 1f / 2f, z }, { -x, -y, -z }, { x, y + 1f / 2f, -z },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, -y, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y, -z + 1f / 2f }, };
	}

	public static float[][] pos16(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, };
	}

	public static float[][] pos17(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, { -x, y, -z + 1f / 2f }, { x, -y, -z }, };
	}

	public static float[][] pos18(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, };
	}

	public static float[][] pos19(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, };
	}

	public static float[][] pos20(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, { -x, y, -z + 1f / 2f }, { x, -y, -z },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, -z }, };
	}

	public static float[][] pos21(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, };
	}

	public static float[][] pos22(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y, z + 1f / 2f }, { -x + 1f / 2f, -y, z + 1f / 2f },
				{ -x + 1f / 2f, y, -z + 1f / 2f }, { x + 1f / 2f, -y, -z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, z },
				{ -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, };
	}

	public static float[][] pos23(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos24(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y, -z }, { x, -y, -z + 1f / 2f }, };
	}

	public static float[][] pos25(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x, -y, z }, { -x, y, z }, };
	}

	public static float[][] pos26(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, { x, -y, z + 1f / 2f }, { -x, y, z }, };
	}

	public static float[][] pos27(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x, -y, z + 1f / 2f }, { -x, y, z + 1f / 2f }, };
	}

	public static float[][] pos28(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x + 1f / 2f, -y, z }, { -x + 1f / 2f, y, z }, };
	}

	public static float[][] pos29(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, { x + 1f / 2f, -y, z },
				{ -x + 1f / 2f, y, z + 1f / 2f }, };
	}

	public static float[][] pos30(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x, -y + 1f / 2f, z + 1f / 2f },
				{ -x, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos31(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { x + 1f / 2f, -y, z + 1f / 2f },
				{ -x, y, z }, };
	}

	public static float[][] pos32(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z }, };
	}

	public static float[][] pos33(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos34(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos35(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x, -y, z }, { -x, y, z }, { x + 1f / 2f, y + 1f / 2f, z },
				{ -x + 1f / 2f, -y + 1f / 2f, z }, { x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z }, };
	}

	public static float[][] pos36(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, { x, -y, z + 1f / 2f }, { -x, y, z },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z }, };
	}

	public static float[][] pos37(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x, -y, z + 1f / 2f }, { -x, y, z + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos38(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x, -y, z }, { -x, y, z }, { x, y + 1f / 2f, z + 1f / 2f },
				{ -x, -y + 1f / 2f, z + 1f / 2f }, { x, -y + 1f / 2f, z + 1f / 2f },
				{ -x, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos39(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x, -y + 1f / 2f, z }, { -x, y + 1f / 2f, z },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z + 1f / 2f }, { x, -y, z + 1f / 2f },
				{ -x, y, z + 1f / 2f }, };
	}

	public static float[][] pos40(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x + 1f / 2f, -y, z }, { -x + 1f / 2f, y, z },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos41(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z }, { x, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, -y, z + 1f / 2f }, { -x + 1f / 2f, y, z + 1f / 2f }, };
	}

	public static float[][] pos42(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x, -y, z }, { -x, y, z }, { x, y + 1f / 2f, z + 1f / 2f },
				{ -x, -y + 1f / 2f, z + 1f / 2f }, { x, -y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, y, z + 1f / 2f }, { -x + 1f / 2f, -y, z + 1f / 2f }, { x + 1f / 2f, -y, z + 1f / 2f },
				{ -x + 1f / 2f, y, z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z },
				{ x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, z }, };
	}

	public static float[][] pos43(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x + 1f / 4f, -y + 1f / 4f, z + 1f / 4f },
				{ -x + 1f / 4f, y + 1f / 4f, z + 1f / 4f }, { x, y + 1f / 2f, z + 1f / 2f },
				{ -x, -y + 1f / 2f, z + 1f / 2f }, { x + 1f / 4f, -y + 3f / 4f, z + 3f / 4f },
				{ -x + 1f / 4f, y + 3f / 4f, z + 3f / 4f }, { x + 1f / 2f, y, z + 1f / 2f },
				{ -x + 1f / 2f, -y, z + 1f / 2f }, { x + 3f / 4f, -y + 1f / 4f, z + 3f / 4f },
				{ -x + 3f / 4f, y + 1f / 4f, z + 3f / 4f }, { x + 1f / 2f, y + 1f / 2f, z },
				{ -x + 1f / 2f, -y + 1f / 2f, z }, { x + 3f / 4f, -y + 3f / 4f, z + 1f / 4f },
				{ -x + 3f / 4f, y + 3f / 4f, z + 1f / 4f }, };
	}

	public static float[][] pos44(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x, -y, z }, { -x, y, z },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos45(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { x, -y, z + 1f / 2f }, { -x, y, z + 1f / 2f }, };
	}

	public static float[][] pos46(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { x + 1f / 2f, -y, z }, { -x + 1f / 2f, y, z },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ x, -y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos47(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { -x, -y, -z }, { x, y, -z },
				{ x, -y, z }, { -x, y, z }, };
	}

	public static float[][] pos48(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos48alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y, -z + 1f / 2f },
				{ x, -y + 1f / 2f, -z + 1f / 2f }, { -x, -y, -z }, { x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos49(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z + 1f / 2f }, { x, -y, -z + 1f / 2f },
				{ -x, -y, -z }, { x, y, -z }, { x, -y, z + 1f / 2f }, { -x, y, z + 1f / 2f }, };
	}

	public static float[][] pos50(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z },
				{ -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z }, };
	}

	public static float[][] pos50alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y, -z },
				{ x, -y + 1f / 2f, -z }, { -x, -y, -z }, { x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y, z },
				{ -x, y + 1f / 2f, z }, };
	}

	public static float[][] pos51(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z }, { -x, y, -z }, { x + 1f / 2f, -y, -z },
				{ -x, -y, -z }, { x + 1f / 2f, y, -z }, { x, -y, z }, { -x + 1f / 2f, y, z }, };
	}

	public static float[][] pos52(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x, -y + 1f / 2f, -z + 1f / 2f }, { -x, -y, -z }, { x + 1f / 2f, y, -z },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos53(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x + 1f / 2f, y, -z + 1f / 2f },
				{ x, -y, -z }, { -x, -y, -z }, { x + 1f / 2f, y, -z + 1f / 2f }, { x + 1f / 2f, -y, z + 1f / 2f },
				{ -x, y, z }, };
	}

	public static float[][] pos54(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z }, { -x, y, -z + 1f / 2f },
				{ x + 1f / 2f, -y, -z + 1f / 2f }, { -x, -y, -z }, { x + 1f / 2f, y, -z }, { x, -y, z + 1f / 2f },
				{ -x + 1f / 2f, y, z + 1f / 2f }, };
	}

	public static float[][] pos55(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { -x, -y, -z }, { x, y, -z }, { x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z }, };
	}

	public static float[][] pos56(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y, -z + 1f / 2f }, { -x, -y, -z }, { x + 1f / 2f, y + 1f / 2f, -z },
				{ x, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y, z + 1f / 2f }, };
	}

	public static float[][] pos57(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x, -y + 1f / 2f, -z }, { -x, -y, -z }, { x, y, -z + 1f / 2f }, { x, -y + 1f / 2f, z + 1f / 2f },
				{ -x, y + 1f / 2f, z }, };
	}

	public static float[][] pos58(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { -x, -y, -z }, { x, y, -z },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos59(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y + 1f / 2f, -z },
				{ x, -y, z }, { -x, y, z }, };
	}

	public static float[][] pos59alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y, -z }, { -x, -y, -z }, { x + 1f / 2f, y + 1f / 2f, -z }, { x, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y, z }, };
	}

	public static float[][] pos60(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x, y, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { -x, -y, -z }, { x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x, -y, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z }, };
	}

	public static float[][] pos61(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { -x, -y, -z }, { x + 1f / 2f, y, -z + 1f / 2f },
				{ x, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z }, };
	}

	public static float[][] pos62(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { -x, -y, -z }, { x + 1f / 2f, y, -z + 1f / 2f },
				{ x, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos63(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, { -x, y, -z + 1f / 2f }, { x, -y, -z },
				{ -x, -y, -z }, { x, y, -z + 1f / 2f }, { x, -y, z + 1f / 2f }, { -x, y, z },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, -z },
				{ -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z }, };
	}

	public static float[][] pos64(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x, -y, -z }, { -x, -y, -z }, { x, y + 1f / 2f, -z + 1f / 2f }, { x, -y + 1f / 2f, z + 1f / 2f },
				{ -x, y, z }, { x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y, z + 1f / 2f },
				{ -x + 1f / 2f, y, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, -z },
				{ -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y, -z + 1f / 2f }, { x + 1f / 2f, -y, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z }, };
	}

	public static float[][] pos65(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { -x, -y, -z }, { x, y, -z },
				{ x, -y, z }, { -x, y, z }, { x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, -z },
				{ -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z }, };
	}

	public static float[][] pos66(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z + 1f / 2f }, { x, -y, -z + 1f / 2f },
				{ -x, -y, -z }, { x, y, -z }, { x, -y, z + 1f / 2f }, { -x, y, z + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos67(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y + 1f / 2f, z }, { -x, y + 1f / 2f, -z }, { x, -y, -z },
				{ -x, -y, -z }, { x, y + 1f / 2f, -z }, { x, -y + 1f / 2f, z }, { -x, y, z },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y, z }, { -x + 1f / 2f, y, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y, -z },
				{ x + 1f / 2f, -y, z }, { -x + 1f / 2f, y + 1f / 2f, z }, };
	}

	public static float[][] pos68(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x, y, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { -x, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y, -z + 1f / 2f },
				{ x, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y, z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, z },
				{ -x, -y, z }, { -x + 1f / 2f, y + 1f / 2f, -z }, { x, -y, -z }, { -x + 1f / 2f, -y, -z + 1f / 2f },
				{ x, y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos68alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z }, { -x, y, -z + 1f / 2f },
				{ x + 1f / 2f, -y, -z + 1f / 2f }, { -x, -y, -z }, { x + 1f / 2f, y, -z }, { x, -y, z + 1f / 2f },
				{ -x + 1f / 2f, y, z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, z }, { -x, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { x, -y + 1f / 2f, -z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z }, { x, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos69(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { -x, -y, -z }, { x, y, -z },
				{ x, -y, z }, { -x, y, z }, { x, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z + 1f / 2f },
				{ -x, y + 1f / 2f, -z + 1f / 2f }, { x, -y + 1f / 2f, -z + 1f / 2f },
				{ -x, -y + 1f / 2f, -z + 1f / 2f }, { x, y + 1f / 2f, -z + 1f / 2f }, { x, -y + 1f / 2f, z + 1f / 2f },
				{ -x, y + 1f / 2f, z + 1f / 2f }, { x + 1f / 2f, y, z + 1f / 2f }, { -x + 1f / 2f, -y, z + 1f / 2f },
				{ -x + 1f / 2f, y, -z + 1f / 2f }, { x + 1f / 2f, -y, -z + 1f / 2f },
				{ -x + 1f / 2f, -y, -z + 1f / 2f }, { x + 1f / 2f, y, -z + 1f / 2f }, { x + 1f / 2f, -y, z + 1f / 2f },
				{ -x + 1f / 2f, y, z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, -z },
				{ -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z }, };
	}

	public static float[][] pos70(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z },
				{ -x + 1f / 4f, -y + 1f / 4f, -z + 1f / 4f }, { x + 1f / 4f, y + 1f / 4f, -z + 1f / 4f },
				{ x + 1f / 4f, -y + 1f / 4f, z + 1f / 4f }, { -x + 1f / 4f, y + 1f / 4f, z + 1f / 4f },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x, -y + 1f / 2f, -z + 1f / 2f }, { -x + 1f / 4f, -y + 3f / 4f, -z + 3f / 4f },
				{ x + 1f / 4f, y + 3f / 4f, -z + 3f / 4f }, { x + 1f / 4f, -y + 3f / 4f, z + 3f / 4f },
				{ -x + 1f / 4f, y + 3f / 4f, z + 3f / 4f }, { x + 1f / 2f, y, z + 1f / 2f },
				{ -x + 1f / 2f, -y, z + 1f / 2f }, { -x + 1f / 2f, y, -z + 1f / 2f }, { x + 1f / 2f, -y, -z + 1f / 2f },
				{ -x + 3f / 4f, -y + 1f / 4f, -z + 3f / 4f }, { x + 3f / 4f, y + 1f / 4f, -z + 3f / 4f },
				{ x + 3f / 4f, -y + 1f / 4f, z + 3f / 4f }, { -x + 3f / 4f, y + 1f / 4f, z + 3f / 4f },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { -x + 3f / 4f, -y + 3f / 4f, -z + 1f / 4f },
				{ x + 3f / 4f, y + 3f / 4f, -z + 1f / 4f }, { x + 3f / 4f, -y + 3f / 4f, z + 1f / 4f },
				{ -x + 3f / 4f, y + 3f / 4f, z + 1f / 4f }, };
	}

	public static float[][] pos70alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 3f / 4f, -y + 3f / 4f, z }, { -x + 3f / 4f, y, -z + 3f / 4f },
				{ x, -y + 3f / 4f, -z + 3f / 4f }, { -x, -y, -z }, { x + 1f / 4f, y + 1f / 4f, -z },
				{ x + 1f / 4f, -y, z + 1f / 4f }, { -x, y + 1f / 4f, z + 1f / 4f }, { x, y + 1f / 2f, z + 1f / 2f },
				{ -x + 3f / 4f, -y + 1f / 4f, z + 1f / 2f }, { -x + 3f / 4f, y + 1f / 2f, -z + 1f / 4f },
				{ x, -y + 1f / 4f, -z + 1f / 4f }, { -x, -y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 4f, y + 3f / 4f, -z + 1f / 2f }, { x + 1f / 4f, -y + 1f / 2f, z + 3f / 4f },
				{ -x, y + 3f / 4f, z + 3f / 4f }, { x + 1f / 2f, y, z + 1f / 2f },
				{ -x + 1f / 4f, -y + 3f / 4f, z + 1f / 2f }, { -x + 1f / 4f, y, -z + 1f / 4f },
				{ x + 1f / 2f, -y + 3f / 4f, -z + 1f / 4f }, { -x + 1f / 2f, -y, -z + 1f / 2f },
				{ x + 3f / 4f, y + 1f / 4f, -z + 1f / 2f }, { x + 3f / 4f, -y, z + 3f / 4f },
				{ -x + 1f / 2f, y + 1f / 4f, z + 3f / 4f }, { x + 1f / 2f, y + 1f / 2f, z },
				{ -x + 1f / 4f, -y + 1f / 4f, z }, { -x + 1f / 4f, y + 1f / 2f, -z + 3f / 4f },
				{ x + 1f / 2f, -y + 1f / 4f, -z + 3f / 4f }, { -x + 1f / 2f, -y + 1f / 2f, -z },
				{ x + 3f / 4f, y + 3f / 4f, -z }, { x + 3f / 4f, -y + 1f / 2f, z + 1f / 4f },
				{ -x + 1f / 2f, y + 3f / 4f, z + 1f / 4f }, };
	}

	public static float[][] pos71(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { -x, -y, -z }, { x, y, -z },
				{ x, -y, z }, { -x, y, z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos72(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { -x, -y, -z }, { x, y, -z }, { x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x, y, -z + 1f / 2f }, { x, -y, -z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x, -y, z + 1f / 2f }, { -x, y, z + 1f / 2f }, };
	}

	public static float[][] pos73(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { -x, -y, -z }, { x + 1f / 2f, y, -z + 1f / 2f },
				{ x, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z }, { -x + 1f / 2f, y, -z },
				{ x, -y, -z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y, z }, { -x, y, z + 1f / 2f }, };
	}

	public static float[][] pos74(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y + 1f / 2f, z }, { -x, y + 1f / 2f, -z }, { x, -y, -z },
				{ -x, -y, -z }, { x, y + 1f / 2f, -z }, { x, -y + 1f / 2f, z }, { -x, y, z },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, -y, z + 1f / 2f },
				{ -x + 1f / 2f, y, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y, -z + 1f / 2f },
				{ x + 1f / 2f, -y, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos75(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, };
	}

	public static float[][] pos76(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, { -y, x, z + 1f / 4f }, { y, -x, z + 3f / 4f }, };
	}

	public static float[][] pos77(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z + 1f / 2f }, { y, -x, z + 1f / 2f }, };
	}

	public static float[][] pos78(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, { -y, x, z + 3f / 4f }, { y, -x, z + 1f / 4f }, };
	}

	public static float[][] pos79(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, { y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos80(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -y, x + 1f / 2f, z + 1f / 4f }, { y + 1f / 2f, -x, z + 3f / 4f },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x, -y, z }, { -y + 1f / 2f, x, z + 3f / 4f },
				{ y, -x + 1f / 2f, z + 1f / 4f }, };
	}

	public static float[][] pos81(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { y, -x, -z }, { -y, x, -z }, };
	}

	public static float[][] pos82(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { y, -x, -z }, { -y, x, -z },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos83(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, { -x, -y, -z }, { x, y, -z },
				{ y, -x, -z }, { -y, x, -z }, };
	}

	public static float[][] pos84(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z + 1f / 2f }, { y, -x, z + 1f / 2f },
				{ -x, -y, -z }, { x, y, -z }, { y, -x, -z + 1f / 2f }, { -y, x, -z + 1f / 2f }, };
	}

	public static float[][] pos85(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y + 1f / 2f, x + 1f / 2f, z },
				{ y + 1f / 2f, -x + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y + 1f / 2f, -z },
				{ y, -x, -z }, { -y, x, -z }, };
	}

	public static float[][] pos85alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -y + 1f / 2f, x, z },
				{ y, -x + 1f / 2f, z }, { -x, -y, -z }, { x + 1f / 2f, y + 1f / 2f, -z }, { y + 1f / 2f, -x, -z },
				{ -y, x + 1f / 2f, -z }, };
	}

	public static float[][] pos86(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { y, -x, -z }, { -y, x, -z }, };
	}

	public static float[][] pos86alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -y, x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x, z + 1f / 2f }, { -x, -y, -z }, { x + 1f / 2f, y + 1f / 2f, -z },
				{ y, -x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, x, -z + 1f / 2f }, };
	}

	public static float[][] pos87(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, { -x, -y, -z }, { x, y, -z },
				{ y, -x, -z }, { -y, x, -z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos88(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -y, x + 1f / 2f, z + 1f / 4f }, { y + 1f / 2f, -x, z + 3f / 4f }, { -x, -y + 1f / 2f, -z + 1f / 4f },
				{ x + 1f / 2f, y, -z + 3f / 4f }, { y, -x, -z }, { -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x, -y, z }, { -y + 1f / 2f, x, z + 3f / 4f },
				{ y, -x + 1f / 2f, z + 1f / 4f }, { -x + 1f / 2f, -y, -z + 3f / 4f }, { x, y + 1f / 2f, -z + 1f / 4f },
				{ y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { -y, x, -z }, };
	}

	public static float[][] pos88alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f },
				{ -y + 3f / 4f, x + 1f / 4f, z + 1f / 4f }, { y + 3f / 4f, -x + 3f / 4f, z + 3f / 4f }, { -x, -y, -z },
				{ x + 1f / 2f, y, -z + 1f / 2f }, { y + 1f / 4f, -x + 3f / 4f, -z + 3f / 4f },
				{ -y + 1f / 4f, x + 1f / 4f, -z + 1f / 4f }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x, -y + 1f / 2f, z }, { -y + 1f / 4f, x + 3f / 4f, z + 3f / 4f },
				{ y + 1f / 4f, -x + 1f / 4f, z + 1f / 4f }, { -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ x, y + 1f / 2f, -z }, { y + 3f / 4f, -x + 1f / 4f, -z + 1f / 4f },
				{ -y + 3f / 4f, x + 3f / 4f, -z + 3f / 4f }, };
	}

	public static float[][] pos89(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, { -x, y, -z }, { x, -y, -z },
				{ y, x, -z }, { -y, -x, -z }, };
	}

	public static float[][] pos90(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y + 1f / 2f, x + 1f / 2f, z },
				{ y + 1f / 2f, -x + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, -z },
				{ y, x, -z }, { -y, -x, -z }, };
	}

	public static float[][] pos91(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, { -y, x, z + 1f / 4f }, { y, -x, z + 3f / 4f },
				{ -x, y, -z }, { x, -y, -z + 1f / 2f }, { y, x, -z + 3f / 4f }, { -y, -x, -z + 1f / 4f }, };
	}

	public static float[][] pos92(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 4f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 3f / 4f }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 4f },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 3f / 4f }, { y, x, -z }, { -y, -x, -z + 1f / 2f }, };
	}

	public static float[][] pos93(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z + 1f / 2f }, { y, -x, z + 1f / 2f },
				{ -x, y, -z }, { x, -y, -z }, { y, x, -z + 1f / 2f }, { -y, -x, -z + 1f / 2f }, };
	}

	public static float[][] pos94(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { y, x, -z }, { -y, -x, -z }, };
	}

	public static float[][] pos95(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, { -y, x, z + 3f / 4f }, { y, -x, z + 1f / 4f },
				{ -x, y, -z }, { x, -y, -z + 1f / 2f }, { y, x, -z + 1f / 4f }, { -y, -x, -z + 3f / 4f }, };
	}

	public static float[][] pos96(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, z + 3f / 4f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 4f }, { -x + 1f / 2f, y + 1f / 2f, -z + 3f / 4f },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 1f / 4f }, { y, x, -z }, { -y, -x, -z + 1f / 2f }, };
	}

	public static float[][] pos97(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, { -x, y, -z }, { x, -y, -z },
				{ y, x, -z }, { -y, -x, -z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f },
				{ -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos98(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -y, x + 1f / 2f, z + 1f / 4f }, { y + 1f / 2f, -x, z + 3f / 4f }, { -x + 1f / 2f, y, -z + 3f / 4f },
				{ x, -y + 1f / 2f, -z + 1f / 4f }, { y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { -y, -x, -z },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x, -y, z }, { -y + 1f / 2f, x, z + 3f / 4f },
				{ y, -x + 1f / 2f, z + 1f / 4f }, { -x, y + 1f / 2f, -z + 1f / 4f }, { x + 1f / 2f, -y, -z + 3f / 4f },
				{ y, x, -z }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos99(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, { x, -y, z }, { -x, y, z },
				{ -y, -x, z }, { y, x, z }, };
	}

	public static float[][] pos100(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, { x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z }, { -y + 1f / 2f, -x + 1f / 2f, z }, { y + 1f / 2f, x + 1f / 2f, z }, };
	}

	public static float[][] pos101(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z + 1f / 2f }, { y, -x, z + 1f / 2f },
				{ x, -y, z + 1f / 2f }, { -x, y, z + 1f / 2f }, { -y, -x, z }, { y, x, z }, };
	}

	public static float[][] pos102(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -y, -x, z }, { y, x, z }, };
	}

	public static float[][] pos103(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, { x, -y, z + 1f / 2f },
				{ -x, y, z + 1f / 2f }, { -y, -x, z + 1f / 2f }, { y, x, z + 1f / 2f }, };
	}

	public static float[][] pos104(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos105(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z + 1f / 2f }, { y, -x, z + 1f / 2f }, { x, -y, z },
				{ -x, y, z }, { -y, -x, z + 1f / 2f }, { y, x, z + 1f / 2f }, };
	}

	public static float[][] pos106(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z + 1f / 2f }, { y, -x, z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, z },
				{ -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos107(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, { x, -y, z }, { -x, y, z },
				{ -y, -x, z }, { y, x, z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos108(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, { x, -y, z + 1f / 2f },
				{ -x, y, z + 1f / 2f }, { -y, -x, z + 1f / 2f }, { y, x, z + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, { y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, z }, { -y + 1f / 2f, -x + 1f / 2f, z },
				{ y + 1f / 2f, x + 1f / 2f, z }, };
	}

	public static float[][] pos109(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -y, x + 1f / 2f, z + 1f / 4f }, { y + 1f / 2f, -x, z + 3f / 4f }, { x, -y, z },
				{ -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -y, -x + 1f / 2f, z + 1f / 4f },
				{ y + 1f / 2f, x, z + 3f / 4f }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x, -y, z },
				{ -y + 1f / 2f, x, z + 3f / 4f }, { y, -x + 1f / 2f, z + 1f / 4f },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x, y, z }, { -y + 1f / 2f, -x, z + 3f / 4f },
				{ y, x + 1f / 2f, z + 1f / 4f }, };
	}

	public static float[][] pos110(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -y, x + 1f / 2f, z + 1f / 4f }, { y + 1f / 2f, -x, z + 3f / 4f }, { x, -y, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z }, { -y, -x + 1f / 2f, z + 3f / 4f }, { y + 1f / 2f, x, z + 1f / 4f },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x, -y, z }, { -y + 1f / 2f, x, z + 3f / 4f },
				{ y, -x + 1f / 2f, z + 1f / 4f }, { x + 1f / 2f, -y + 1f / 2f, z }, { -x, y, z + 1f / 2f },
				{ -y + 1f / 2f, -x, z + 1f / 4f }, { y, x + 1f / 2f, z + 3f / 4f }, };
	}

	public static float[][] pos111(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { y, -x, -z }, { -y, x, -z }, { -x, y, -z }, { x, -y, -z },
				{ -y, -x, z }, { y, x, z }, };
	}

	public static float[][] pos112(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { y, -x, -z }, { -y, x, -z }, { -x, y, -z + 1f / 2f },
				{ x, -y, -z + 1f / 2f }, { -y, -x, z + 1f / 2f }, { y, x, z + 1f / 2f }, };
	}

	public static float[][] pos113(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { y, -x, -z }, { -y, x, -z },
				{ -x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, -z }, { -y + 1f / 2f, -x + 1f / 2f, z },
				{ y + 1f / 2f, x + 1f / 2f, z }, };
	}

	public static float[][] pos114(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { y, -x, -z }, { -y, x, -z },
				{ -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos115(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { y, -x, -z }, { -y, x, -z }, { x, -y, z }, { -x, y, z },
				{ y, x, -z }, { -y, -x, -z }, };
	}

	public static float[][] pos116(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { y, -x, -z }, { -y, x, -z }, { x, -y, z + 1f / 2f },
				{ -x, y, z + 1f / 2f }, { y, x, -z + 1f / 2f }, { -y, -x, -z + 1f / 2f }, };
	}

	public static float[][] pos117(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { y, -x, -z }, { -y, x, -z },
				{ x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, z }, { y + 1f / 2f, x + 1f / 2f, -z },
				{ -y + 1f / 2f, -x + 1f / 2f, -z }, };
	}

	public static float[][] pos118(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { y, -x, -z }, { -y, x, -z },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos119(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { y, -x, -z }, { -y, x, -z }, { x, -y, z }, { -x, y, z },
				{ y, x, -z }, { -y, -x, -z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f },
				{ -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, };
	}

	public static float[][] pos120(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { y, -x, -z }, { -y, x, -z }, { x, -y, z + 1f / 2f },
				{ -x, y, z + 1f / 2f }, { y, x, -z + 1f / 2f }, { -y, -x, -z + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, z }, { y + 1f / 2f, x + 1f / 2f, -z },
				{ -y + 1f / 2f, -x + 1f / 2f, -z }, };
	}

	public static float[][] pos121(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { y, -x, -z }, { -y, x, -z }, { -x, y, -z }, { x, -y, -z },
				{ -y, -x, z }, { y, x, z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos122(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { y, -x, -z }, { -y, x, -z },
				{ -x + 1f / 2f, y, -z + 3f / 4f }, { x + 1f / 2f, -y, -z + 3f / 4f }, { -y + 1f / 2f, -x, z + 3f / 4f },
				{ y + 1f / 2f, x, z + 3f / 4f }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 4f },
				{ x, -y + 1f / 2f, -z + 1f / 4f }, { -y, -x + 1f / 2f, z + 1f / 4f },
				{ y, x + 1f / 2f, z + 1f / 4f }, };
	}

	public static float[][] pos123(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, { -x, y, -z }, { x, -y, -z },
				{ y, x, -z }, { -y, -x, -z }, { -x, -y, -z }, { x, y, -z }, { y, -x, -z }, { -y, x, -z }, { x, -y, z },
				{ -x, y, z }, { -y, -x, z }, { y, x, z }, };
	}

	public static float[][] pos124(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, { -x, y, -z + 1f / 2f },
				{ x, -y, -z + 1f / 2f }, { y, x, -z + 1f / 2f }, { -y, -x, -z + 1f / 2f }, { -x, -y, -z }, { x, y, -z },
				{ y, -x, -z }, { -y, x, -z }, { x, -y, z + 1f / 2f }, { -x, y, z + 1f / 2f }, { -y, -x, z + 1f / 2f },
				{ y, x, z + 1f / 2f }, };
	}

	public static float[][] pos125(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, { -x, y, -z }, { x, -y, -z },
				{ y, x, -z }, { -y, -x, -z }, { -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y + 1f / 2f, -z },
				{ y + 1f / 2f, -x + 1f / 2f, -z }, { -y + 1f / 2f, x + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z }, { -y + 1f / 2f, -x + 1f / 2f, z }, { y + 1f / 2f, x + 1f / 2f, z }, };
	}

	public static float[][] pos125alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -y + 1f / 2f, x, z },
				{ y, -x + 1f / 2f, z }, { -x + 1f / 2f, y, -z }, { x, -y + 1f / 2f, -z }, { y, x, -z },
				{ -y + 1f / 2f, -x + 1f / 2f, -z }, { -x, -y, -z }, { x + 1f / 2f, y + 1f / 2f, -z },
				{ y + 1f / 2f, -x, -z }, { -y, x + 1f / 2f, -z }, { x + 1f / 2f, -y, z }, { -x, y + 1f / 2f, z },
				{ -y, -x, z }, { y + 1f / 2f, x + 1f / 2f, z }, };
	}

	public static float[][] pos126(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, { -x, y, -z }, { x, -y, -z },
				{ y, x, -z }, { -y, -x, -z }, { -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos126alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -y + 1f / 2f, x, z },
				{ y, -x + 1f / 2f, z }, { -x + 1f / 2f, y, -z + 1f / 2f }, { x, -y + 1f / 2f, -z + 1f / 2f },
				{ y, x, -z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { -x, -y, -z },
				{ x + 1f / 2f, y + 1f / 2f, -z }, { y + 1f / 2f, -x, -z }, { -y, x + 1f / 2f, -z },
				{ x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, z + 1f / 2f }, { -y, -x, z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos127(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z },
				{ -x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, -z }, { y + 1f / 2f, x + 1f / 2f, -z },
				{ -y + 1f / 2f, -x + 1f / 2f, -z }, { -x, -y, -z }, { x, y, -z }, { y, -x, -z }, { -y, x, -z },
				{ x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, z }, { -y + 1f / 2f, -x + 1f / 2f, z },
				{ y + 1f / 2f, x + 1f / 2f, z }, };
	}

	public static float[][] pos128(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z },
				{ -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ -x, -y, -z }, { x, y, -z }, { y, -x, -z }, { -y, x, -z }, { x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos129(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y + 1f / 2f, x + 1f / 2f, z },
				{ y + 1f / 2f, -x + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, -z },
				{ y, x, -z }, { -y, -x, -z }, { -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y + 1f / 2f, -z },
				{ y, -x, -z }, { -y, x, -z }, { x, -y, z }, { -x, y, z }, { -y + 1f / 2f, -x + 1f / 2f, z },
				{ y + 1f / 2f, x + 1f / 2f, z }, };
	}

	public static float[][] pos129alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -y + 1f / 2f, x, z },
				{ y, -x + 1f / 2f, z }, { -x, y + 1f / 2f, -z }, { x + 1f / 2f, -y, -z },
				{ y + 1f / 2f, x + 1f / 2f, -z }, { -y, -x, -z }, { -x, -y, -z }, { x + 1f / 2f, y + 1f / 2f, -z },
				{ y + 1f / 2f, -x, -z }, { -y, x + 1f / 2f, -z }, { x, -y + 1f / 2f, z }, { -x + 1f / 2f, y, z },
				{ -y + 1f / 2f, -x + 1f / 2f, z }, { y, x, z }, };
	}

	public static float[][] pos130(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y + 1f / 2f, x + 1f / 2f, z },
				{ y + 1f / 2f, -x + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { y, x, -z + 1f / 2f }, { -y, -x, -z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y + 1f / 2f, -z }, { y, -x, -z }, { -y, x, -z },
				{ x, -y, z + 1f / 2f }, { -x, y, z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos130alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -y + 1f / 2f, x, z },
				{ y, -x + 1f / 2f, z }, { -x, y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y, -z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { -y, -x, -z + 1f / 2f }, { -x, -y, -z },
				{ x + 1f / 2f, y + 1f / 2f, -z }, { y + 1f / 2f, -x, -z }, { -y, x + 1f / 2f, -z },
				{ x, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y, z + 1f / 2f },
				{ -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { y, x, z + 1f / 2f }, };
	}

	public static float[][] pos131(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z + 1f / 2f }, { y, -x, z + 1f / 2f },
				{ -x, y, -z }, { x, -y, -z }, { y, x, -z + 1f / 2f }, { -y, -x, -z + 1f / 2f }, { -x, -y, -z },
				{ x, y, -z }, { y, -x, -z + 1f / 2f }, { -y, x, -z + 1f / 2f }, { x, -y, z }, { -x, y, z },
				{ -y, -x, z + 1f / 2f }, { y, x, z + 1f / 2f }, };
	}

	public static float[][] pos132(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z + 1f / 2f }, { y, -x, z + 1f / 2f },
				{ -x, y, -z + 1f / 2f }, { x, -y, -z + 1f / 2f }, { y, x, -z }, { -y, -x, -z }, { -x, -y, -z },
				{ x, y, -z }, { y, -x, -z + 1f / 2f }, { -y, x, -z + 1f / 2f }, { x, -y, z + 1f / 2f },
				{ -x, y, z + 1f / 2f }, { -y, -x, z }, { y, x, z }, };
	}

	public static float[][] pos133(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -x, y, -z + 1f / 2f }, { x, -y, -z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, -z }, { -y + 1f / 2f, -x + 1f / 2f, -z },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { y, -x, -z },
				{ -y, x, -z }, { x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, z },
				{ -y, -x, z + 1f / 2f }, { y, x, z + 1f / 2f }, };
	}

	public static float[][] pos133alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -y + 1f / 2f, x, z + 1f / 2f },
				{ y, -x + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y, -z }, { x, -y + 1f / 2f, -z },
				{ y, x, -z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { -x, -y, -z },
				{ x + 1f / 2f, y + 1f / 2f, -z }, { y + 1f / 2f, -x, -z + 1f / 2f }, { -y, x + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y, z }, { -x, y + 1f / 2f, z }, { -y, -x, z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos134(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -x, y, -z }, { x, -y, -z },
				{ y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { y, -x, -z },
				{ -y, x, -z }, { x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -y, -x, z }, { y, x, z }, };
	}

	public static float[][] pos134alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -y + 1f / 2f, x, z + 1f / 2f },
				{ y, -x + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y, -z + 1f / 2f }, { x, -y + 1f / 2f, -z + 1f / 2f },
				{ y, x, -z }, { -y + 1f / 2f, -x + 1f / 2f, -z }, { -x, -y, -z }, { x + 1f / 2f, y + 1f / 2f, -z },
				{ y + 1f / 2f, -x, -z + 1f / 2f }, { -y, x + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y, z + 1f / 2f },
				{ -x, y + 1f / 2f, z + 1f / 2f }, { -y, -x, z }, { y + 1f / 2f, x + 1f / 2f, z }, };
	}

	public static float[][] pos135(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z + 1f / 2f }, { y, -x, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, -z },
				{ y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ -x, -y, -z }, { x, y, -z }, { y, -x, -z + 1f / 2f }, { -y, x, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, z },
				{ -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos136(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { y, x, -z }, { -y, -x, -z }, { -x, -y, -z }, { x, y, -z },
				{ y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -y, -x, z },
				{ y, x, z }, };
	}

	public static float[][] pos137(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { y, x, -z }, { -y, -x, -z },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { y, -x, -z },
				{ -y, x, -z }, { x, -y, z }, { -x, y, z }, { -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos137alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -y + 1f / 2f, x, z + 1f / 2f },
				{ y, -x + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, -z }, { x + 1f / 2f, -y, -z },
				{ y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { -y, -x, -z + 1f / 2f }, { -x, -y, -z },
				{ x + 1f / 2f, y + 1f / 2f, -z }, { y + 1f / 2f, -x, -z + 1f / 2f }, { -y, x + 1f / 2f, -z + 1f / 2f },
				{ x, -y + 1f / 2f, z }, { -x + 1f / 2f, y, z }, { -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f },
				{ y, x, z + 1f / 2f }, };
	}

	public static float[][] pos138(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { y, x, -z + 1f / 2f }, { -y, -x, -z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { y, -x, -z },
				{ -y, x, -z }, { x, -y, z + 1f / 2f }, { -x, y, z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, z },
				{ y + 1f / 2f, x + 1f / 2f, z }, };
	}

	public static float[][] pos138alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -y + 1f / 2f, x, z + 1f / 2f },
				{ y, -x + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y, -z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, -z }, { -y, -x, -z }, { -x, -y, -z }, { x + 1f / 2f, y + 1f / 2f, -z },
				{ y + 1f / 2f, -x, -z + 1f / 2f }, { -y, x + 1f / 2f, -z + 1f / 2f }, { x, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y, z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, z }, { y, x, z }, };
	}

	public static float[][] pos139(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, { -x, y, -z }, { x, -y, -z },
				{ y, x, -z }, { -y, -x, -z }, { -x, -y, -z }, { x, y, -z }, { y, -x, -z }, { -y, x, -z }, { x, -y, z },
				{ -x, y, z }, { -y, -x, z }, { y, x, z }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f },
				{ -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, };
	}

	public static float[][] pos140(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -y, x, z }, { y, -x, z }, { -x, y, -z + 1f / 2f },
				{ x, -y, -z + 1f / 2f }, { y, x, -z + 1f / 2f }, { -y, -x, -z + 1f / 2f }, { -x, -y, -z }, { x, y, -z },
				{ y, -x, -z }, { -y, x, -z }, { x, -y, z + 1f / 2f }, { -x, y, z + 1f / 2f }, { -y, -x, z + 1f / 2f },
				{ y, x, z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { y + 1f / 2f, x + 1f / 2f, -z }, { -y + 1f / 2f, -x + 1f / 2f, -z },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, z }, { -y + 1f / 2f, -x + 1f / 2f, z },
				{ y + 1f / 2f, x + 1f / 2f, z }, };
	}

	public static float[][] pos141(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -y, x + 1f / 2f, z + 1f / 4f }, { y + 1f / 2f, -x, z + 3f / 4f }, { -x + 1f / 2f, y, -z + 3f / 4f },
				{ x, -y + 1f / 2f, -z + 1f / 4f }, { y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { -y, -x, -z },
				{ -x, -y + 1f / 2f, -z + 1f / 4f }, { x + 1f / 2f, y, -z + 3f / 4f }, { y, -x, -z },
				{ -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x, y, z },
				{ -y + 1f / 2f, -x, z + 3f / 4f }, { y, x + 1f / 2f, z + 1f / 4f },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x, -y, z }, { -y + 1f / 2f, x, z + 3f / 4f },
				{ y, -x + 1f / 2f, z + 1f / 4f }, { -x, y + 1f / 2f, -z + 1f / 4f }, { x + 1f / 2f, -y, -z + 3f / 4f },
				{ y, x, -z }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { -x + 1f / 2f, -y, -z + 3f / 4f },
				{ x, y + 1f / 2f, -z + 1f / 4f }, { y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { -y, x, -z },
				{ x, -y, z }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -y, -x + 1f / 2f, z + 1f / 4f },
				{ y + 1f / 2f, x, z + 3f / 4f }, };
	}

	public static float[][] pos141alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f },
				{ -y + 1f / 4f, x + 3f / 4f, z + 1f / 4f }, { y + 1f / 4f, -x + 1f / 4f, z + 3f / 4f },
				{ -x + 1f / 2f, y, -z + 1f / 2f }, { x, -y, -z }, { y + 1f / 4f, x + 3f / 4f, -z + 1f / 4f },
				{ -y + 1f / 4f, -x + 1f / 4f, -z + 3f / 4f }, { -x, -y, -z }, { x + 1f / 2f, y, -z + 1f / 2f },
				{ y + 3f / 4f, -x + 1f / 4f, -z + 3f / 4f }, { -y + 3f / 4f, x + 3f / 4f, -z + 1f / 4f },
				{ x + 1f / 2f, -y, z + 1f / 2f }, { -x, y, z }, { -y + 3f / 4f, -x + 1f / 4f, z + 3f / 4f },
				{ y + 3f / 4f, x + 3f / 4f, z + 1f / 4f }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x, -y + 1f / 2f, z }, { -y + 3f / 4f, x + 1f / 4f, z + 3f / 4f },
				{ y + 3f / 4f, -x + 3f / 4f, z + 1f / 4f }, { -x, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { y + 3f / 4f, x + 1f / 4f, -z + 3f / 4f },
				{ -y + 3f / 4f, -x + 3f / 4f, -z + 1f / 4f }, { -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ x, y + 1f / 2f, -z }, { y + 1f / 4f, -x + 3f / 4f, -z + 1f / 4f },
				{ -y + 1f / 4f, x + 1f / 4f, -z + 3f / 4f }, { x, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -y + 1f / 4f, -x + 3f / 4f, z + 1f / 4f },
				{ y + 1f / 4f, x + 1f / 4f, z + 3f / 4f }, };
	}

	public static float[][] pos142(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -y, x + 1f / 2f, z + 1f / 4f }, { y + 1f / 2f, -x, z + 3f / 4f }, { -x + 1f / 2f, y, -z + 1f / 4f },
				{ x, -y + 1f / 2f, -z + 3f / 4f }, { y + 1f / 2f, x + 1f / 2f, -z }, { -y, -x, -z + 1f / 2f },
				{ -x, -y + 1f / 2f, -z + 1f / 4f }, { x + 1f / 2f, y, -z + 3f / 4f }, { y, -x, -z },
				{ -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, z }, { -x, y, z + 1f / 2f },
				{ -y + 1f / 2f, -x, z + 1f / 4f }, { y, x + 1f / 2f, z + 3f / 4f },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x, -y, z }, { -y + 1f / 2f, x, z + 3f / 4f },
				{ y, -x + 1f / 2f, z + 1f / 4f }, { -x, y + 1f / 2f, -z + 3f / 4f }, { x + 1f / 2f, -y, -z + 1f / 4f },
				{ y, x, -z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, -z }, { -x + 1f / 2f, -y, -z + 3f / 4f },
				{ x, y + 1f / 2f, -z + 1f / 4f }, { y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { -y, x, -z },
				{ x, -y, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z }, { -y, -x + 1f / 2f, z + 3f / 4f },
				{ y + 1f / 2f, x, z + 1f / 4f }, };
	}

	public static float[][] pos142alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f },
				{ -y + 1f / 4f, x + 3f / 4f, z + 1f / 4f }, { y + 1f / 4f, -x + 1f / 4f, z + 3f / 4f },
				{ -x + 1f / 2f, y, -z }, { x, -y, -z + 1f / 2f }, { y + 1f / 4f, x + 3f / 4f, -z + 3f / 4f },
				{ -y + 1f / 4f, -x + 1f / 4f, -z + 1f / 4f }, { -x, -y, -z }, { x + 1f / 2f, y, -z + 1f / 2f },
				{ y + 3f / 4f, -x + 1f / 4f, -z + 3f / 4f }, { -y + 3f / 4f, x + 3f / 4f, -z + 1f / 4f },
				{ x + 1f / 2f, -y, z }, { -x, y, z + 1f / 2f }, { -y + 3f / 4f, -x + 1f / 4f, z + 1f / 4f },
				{ y + 3f / 4f, x + 3f / 4f, z + 3f / 4f }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x, -y + 1f / 2f, z }, { -y + 3f / 4f, x + 1f / 4f, z + 3f / 4f },
				{ y + 3f / 4f, -x + 3f / 4f, z + 1f / 4f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { y + 3f / 4f, x + 1f / 4f, -z + 1f / 4f },
				{ -y + 3f / 4f, -x + 3f / 4f, -z + 3f / 4f }, { -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ x, y + 1f / 2f, -z }, { y + 1f / 4f, -x + 3f / 4f, -z + 1f / 4f },
				{ -y + 1f / 4f, x + 1f / 4f, -z + 3f / 4f }, { x, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z }, { -y + 1f / 4f, -x + 3f / 4f, z + 3f / 4f },
				{ y + 1f / 4f, x + 1f / 4f, z + 1f / 4f }, };
	}

	public static float[][] pos143(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, };
	}

	public static float[][] pos144(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z + 1f / 3f }, { -x + y, -x, z + 2f / 3f }, };
	}

	public static float[][] pos145(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z + 2f / 3f }, { -x + y, -x, z + 1f / 3f }, };
	}

	public static float[][] pos146(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z },
				{ x + 2f / 3f, y + 1f / 3f, z + 1f / 3f }, { -y + 2f / 3f, x - y + 1f / 3f, z + 1f / 3f },
				{ -x + y + 2f / 3f, -x + 1f / 3f, z + 1f / 3f }, { x + 1f / 3f, y + 2f / 3f, z + 2f / 3f },
				{ -y + 1f / 3f, x - y + 2f / 3f, z + 2f / 3f }, { -x + y + 1f / 3f, -x + 2f / 3f, z + 2f / 3f }, };
	}

	public static float[][] pos146alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { z, x, y }, { y, z, x }, };
	}

	public static float[][] pos147(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, -z }, { y, -x + y, -z },
				{ x - y, x, -z }, };
	}

	public static float[][] pos148(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, -z }, { y, -x + y, -z },
				{ x - y, x, -z }, { x + 2f / 3f, y + 1f / 3f, z + 1f / 3f },
				{ -y + 2f / 3f, x - y + 1f / 3f, z + 1f / 3f }, { -x + y + 2f / 3f, -x + 1f / 3f, z + 1f / 3f },
				{ -x + 2f / 3f, -y + 1f / 3f, -z + 1f / 3f }, { y + 2f / 3f, -x + y + 1f / 3f, -z + 1f / 3f },
				{ x - y + 2f / 3f, x + 1f / 3f, -z + 1f / 3f }, { x + 1f / 3f, y + 2f / 3f, z + 2f / 3f },
				{ -y + 1f / 3f, x - y + 2f / 3f, z + 2f / 3f }, { -x + y + 1f / 3f, -x + 2f / 3f, z + 2f / 3f },
				{ -x + 1f / 3f, -y + 2f / 3f, -z + 2f / 3f }, { y + 1f / 3f, -x + y + 2f / 3f, -z + 2f / 3f },
				{ x - y + 1f / 3f, x + 2f / 3f, -z + 2f / 3f }, };
	}

	public static float[][] pos148alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { z, x, y }, { y, z, x }, { -x, -y, -z }, { -z, -x, -y }, { -y, -z, -x }, };
	}

	public static float[][] pos149(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -y, -x, -z }, { -x + y, y, -z },
				{ x, x - y, -z }, };
	}

	public static float[][] pos150(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { y, x, -z }, { x - y, -y, -z },
				{ -x, -x + y, -z }, };
	}

	public static float[][] pos151(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z + 1f / 3f }, { -x + y, -x, z + 2f / 3f },
				{ -y, -x, -z + 2f / 3f }, { -x + y, y, -z + 1f / 3f }, { x, x - y, -z }, };
	}

	public static float[][] pos152(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z + 1f / 3f }, { -x + y, -x, z + 2f / 3f }, { y, x, -z },
				{ x - y, -y, -z + 2f / 3f }, { -x, -x + y, -z + 1f / 3f }, };
	}

	public static float[][] pos153(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z + 2f / 3f }, { -x + y, -x, z + 1f / 3f },
				{ -y, -x, -z + 1f / 3f }, { -x + y, y, -z + 2f / 3f }, { x, x - y, -z }, };
	}

	public static float[][] pos154(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z + 2f / 3f }, { -x + y, -x, z + 1f / 3f }, { y, x, -z },
				{ x - y, -y, -z + 1f / 3f }, { -x, -x + y, -z + 2f / 3f }, };
	}

	public static float[][] pos155(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { y, x, -z }, { x - y, -y, -z },
				{ -x, -x + y, -z }, { x + 2f / 3f, y + 1f / 3f, z + 1f / 3f },
				{ -y + 2f / 3f, x - y + 1f / 3f, z + 1f / 3f }, { -x + y + 2f / 3f, -x + 1f / 3f, z + 1f / 3f },
				{ y + 2f / 3f, x + 1f / 3f, -z + 1f / 3f }, { x - y + 2f / 3f, -y + 1f / 3f, -z + 1f / 3f },
				{ -x + 2f / 3f, -x + y + 1f / 3f, -z + 1f / 3f }, { x + 1f / 3f, y + 2f / 3f, z + 2f / 3f },
				{ -y + 1f / 3f, x - y + 2f / 3f, z + 2f / 3f }, { -x + y + 1f / 3f, -x + 2f / 3f, z + 2f / 3f },
				{ y + 1f / 3f, x + 2f / 3f, -z + 2f / 3f }, { x - y + 1f / 3f, -y + 2f / 3f, -z + 2f / 3f },
				{ -x + 1f / 3f, -x + y + 2f / 3f, -z + 2f / 3f }, };
	}

	public static float[][] pos155alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { z, x, y }, { y, z, x }, { -y, -x, -z }, { -x, -z, -y }, { -z, -y, -x }, };
	}

	public static float[][] pos156(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -y, -x, z }, { -x + y, y, z },
				{ x, x - y, z }, };
	}

	public static float[][] pos157(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { y, x, z }, { x - y, -y, z },
				{ -x, -x + y, z }, };
	}

	public static float[][] pos158(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -y, -x, z + 1f / 2f },
				{ -x + y, y, z + 1f / 2f }, { x, x - y, z + 1f / 2f }, };
	}

	public static float[][] pos159(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { y, x, z + 1f / 2f },
				{ x - y, -y, z + 1f / 2f }, { -x, -x + y, z + 1f / 2f }, };
	}

	public static float[][] pos160(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -y, -x, z }, { -x + y, y, z },
				{ x, x - y, z }, { x + 2f / 3f, y + 1f / 3f, z + 1f / 3f },
				{ -y + 2f / 3f, x - y + 1f / 3f, z + 1f / 3f }, { -x + y + 2f / 3f, -x + 1f / 3f, z + 1f / 3f },
				{ -y + 2f / 3f, -x + 1f / 3f, z + 1f / 3f }, { -x + y + 2f / 3f, y + 1f / 3f, z + 1f / 3f },
				{ x + 2f / 3f, x - y + 1f / 3f, z + 1f / 3f }, { x + 1f / 3f, y + 2f / 3f, z + 2f / 3f },
				{ -y + 1f / 3f, x - y + 2f / 3f, z + 2f / 3f }, { -x + y + 1f / 3f, -x + 2f / 3f, z + 2f / 3f },
				{ -y + 1f / 3f, -x + 2f / 3f, z + 2f / 3f }, { -x + y + 1f / 3f, y + 2f / 3f, z + 2f / 3f },
				{ x + 1f / 3f, x - y + 2f / 3f, z + 2f / 3f }, };
	}

	public static float[][] pos160alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { z, x, y }, { y, z, x }, { y, x, z }, { x, z, y }, { z, y, x }, };
	}

	public static float[][] pos161(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -y, -x, z + 1f / 2f },
				{ -x + y, y, z + 1f / 2f }, { x, x - y, z + 1f / 2f }, { x + 2f / 3f, y + 1f / 3f, z + 1f / 3f },
				{ -y + 2f / 3f, x - y + 1f / 3f, z + 1f / 3f }, { -x + y + 2f / 3f, -x + 1f / 3f, z + 1f / 3f },
				{ -y + 2f / 3f, -x + 1f / 3f, z + 5f / 6f }, { -x + y + 2f / 3f, y + 1f / 3f, z + 5f / 6f },
				{ x + 2f / 3f, x - y + 1f / 3f, z + 5f / 6f }, { x + 1f / 3f, y + 2f / 3f, z + 2f / 3f },
				{ -y + 1f / 3f, x - y + 2f / 3f, z + 2f / 3f }, { -x + y + 1f / 3f, -x + 2f / 3f, z + 2f / 3f },
				{ -y + 1f / 3f, -x + 2f / 3f, z + 1f / 6f }, { -x + y + 1f / 3f, y + 2f / 3f, z + 1f / 6f },
				{ x + 1f / 3f, x - y + 2f / 3f, z + 1f / 6f }, };
	}

	public static float[][] pos161alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { z, x, y }, { y, z, x }, { y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, z + 1f / 2f, y + 1f / 2f }, { z + 1f / 2f, y + 1f / 2f, x + 1f / 2f }, };
	}

	public static float[][] pos162(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -y, -x, -z }, { -x + y, y, -z },
				{ x, x - y, -z }, { -x, -y, -z }, { y, -x + y, -z }, { x - y, x, -z }, { y, x, z }, { x - y, -y, z },
				{ -x, -x + y, z }, };
	}

	public static float[][] pos163(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -y, -x, -z + 1f / 2f },
				{ -x + y, y, -z + 1f / 2f }, { x, x - y, -z + 1f / 2f }, { -x, -y, -z }, { y, -x + y, -z },
				{ x - y, x, -z }, { y, x, z + 1f / 2f }, { x - y, -y, z + 1f / 2f }, { -x, -x + y, z + 1f / 2f }, };
	}

	public static float[][] pos164(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { y, x, -z }, { x - y, -y, -z },
				{ -x, -x + y, -z }, { -x, -y, -z }, { y, -x + y, -z }, { x - y, x, -z }, { -y, -x, z },
				{ -x + y, y, z }, { x, x - y, z }, };
	}

	public static float[][] pos165(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { y, x, -z + 1f / 2f },
				{ x - y, -y, -z + 1f / 2f }, { -x, -x + y, -z + 1f / 2f }, { -x, -y, -z }, { y, -x + y, -z },
				{ x - y, x, -z }, { -y, -x, z + 1f / 2f }, { -x + y, y, z + 1f / 2f }, { x, x - y, z + 1f / 2f }, };
	}

	public static float[][] pos166(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { y, x, -z }, { x - y, -y, -z },
				{ -x, -x + y, -z }, { -x, -y, -z }, { y, -x + y, -z }, { x - y, x, -z }, { -y, -x, z },
				{ -x + y, y, z }, { x, x - y, z }, { x + 2f / 3f, y + 1f / 3f, z + 1f / 3f },
				{ -y + 2f / 3f, x - y + 1f / 3f, z + 1f / 3f }, { -x + y + 2f / 3f, -x + 1f / 3f, z + 1f / 3f },
				{ y + 2f / 3f, x + 1f / 3f, -z + 1f / 3f }, { x - y + 2f / 3f, -y + 1f / 3f, -z + 1f / 3f },
				{ -x + 2f / 3f, -x + y + 1f / 3f, -z + 1f / 3f }, { -x + 2f / 3f, -y + 1f / 3f, -z + 1f / 3f },
				{ y + 2f / 3f, -x + y + 1f / 3f, -z + 1f / 3f }, { x - y + 2f / 3f, x + 1f / 3f, -z + 1f / 3f },
				{ -y + 2f / 3f, -x + 1f / 3f, z + 1f / 3f }, { -x + y + 2f / 3f, y + 1f / 3f, z + 1f / 3f },
				{ x + 2f / 3f, x - y + 1f / 3f, z + 1f / 3f }, { x + 1f / 3f, y + 2f / 3f, z + 2f / 3f },
				{ -y + 1f / 3f, x - y + 2f / 3f, z + 2f / 3f }, { -x + y + 1f / 3f, -x + 2f / 3f, z + 2f / 3f },
				{ y + 1f / 3f, x + 2f / 3f, -z + 2f / 3f }, { x - y + 1f / 3f, -y + 2f / 3f, -z + 2f / 3f },
				{ -x + 1f / 3f, -x + y + 2f / 3f, -z + 2f / 3f }, { -x + 1f / 3f, -y + 2f / 3f, -z + 2f / 3f },
				{ y + 1f / 3f, -x + y + 2f / 3f, -z + 2f / 3f }, { x - y + 1f / 3f, x + 2f / 3f, -z + 2f / 3f },
				{ -y + 1f / 3f, -x + 2f / 3f, z + 2f / 3f }, { -x + y + 1f / 3f, y + 2f / 3f, z + 2f / 3f },
				{ x + 1f / 3f, x - y + 2f / 3f, z + 2f / 3f }, };
	}

	public static float[][] pos166alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { z, x, y }, { y, z, x }, { -y, -x, -z }, { -x, -z, -y }, { -z, -y, -x },
				{ -x, -y, -z }, { -z, -x, -y }, { -y, -z, -x }, { y, x, z }, { x, z, y }, { z, y, x }, };
	}

	public static float[][] pos167(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { y, x, -z + 1f / 2f },
				{ x - y, -y, -z + 1f / 2f }, { -x, -x + y, -z + 1f / 2f }, { -x, -y, -z }, { y, -x + y, -z },
				{ x - y, x, -z }, { -y, -x, z + 1f / 2f }, { -x + y, y, z + 1f / 2f }, { x, x - y, z + 1f / 2f },
				{ x + 2f / 3f, y + 1f / 3f, z + 1f / 3f }, { -y + 2f / 3f, x - y + 1f / 3f, z + 1f / 3f },
				{ -x + y + 2f / 3f, -x + 1f / 3f, z + 1f / 3f }, { y + 2f / 3f, x + 1f / 3f, -z + 5f / 6f },
				{ x - y + 2f / 3f, -y + 1f / 3f, -z + 5f / 6f }, { -x + 2f / 3f, -x + y + 1f / 3f, -z + 5f / 6f },
				{ -x + 2f / 3f, -y + 1f / 3f, -z + 1f / 3f }, { y + 2f / 3f, -x + y + 1f / 3f, -z + 1f / 3f },
				{ x - y + 2f / 3f, x + 1f / 3f, -z + 1f / 3f }, { -y + 2f / 3f, -x + 1f / 3f, z + 5f / 6f },
				{ -x + y + 2f / 3f, y + 1f / 3f, z + 5f / 6f }, { x + 2f / 3f, x - y + 1f / 3f, z + 5f / 6f },
				{ x + 1f / 3f, y + 2f / 3f, z + 2f / 3f }, { -y + 1f / 3f, x - y + 2f / 3f, z + 2f / 3f },
				{ -x + y + 1f / 3f, -x + 2f / 3f, z + 2f / 3f }, { y + 1f / 3f, x + 2f / 3f, -z + 1f / 6f },
				{ x - y + 1f / 3f, -y + 2f / 3f, -z + 1f / 6f }, { -x + 1f / 3f, -x + y + 2f / 3f, -z + 1f / 6f },
				{ -x + 1f / 3f, -y + 2f / 3f, -z + 2f / 3f }, { y + 1f / 3f, -x + y + 2f / 3f, -z + 2f / 3f },
				{ x - y + 1f / 3f, x + 2f / 3f, -z + 2f / 3f }, { -y + 1f / 3f, -x + 2f / 3f, z + 1f / 6f },
				{ -x + y + 1f / 3f, y + 2f / 3f, z + 1f / 6f }, { x + 1f / 3f, x - y + 2f / 3f, z + 1f / 6f }, };
	}

	public static float[][] pos167alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { z, x, y }, { y, z, x }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ -x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f }, { -z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f },
				{ -x, -y, -z }, { -z, -x, -y }, { -y, -z, -x }, { y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, z + 1f / 2f, y + 1f / 2f }, { z + 1f / 2f, y + 1f / 2f, x + 1f / 2f }, };
	}

	public static float[][] pos168(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, z }, { y, -x + y, z },
				{ x - y, x, z }, };
	}

	public static float[][] pos169(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z + 1f / 3f }, { -x + y, -x, z + 2f / 3f },
				{ -x, -y, z + 1f / 2f }, { y, -x + y, z + 5f / 6f }, { x - y, x, z + 1f / 6f }, };
	}

	public static float[][] pos170(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z + 2f / 3f }, { -x + y, -x, z + 1f / 3f },
				{ -x, -y, z + 1f / 2f }, { y, -x + y, z + 1f / 6f }, { x - y, x, z + 5f / 6f }, };
	}

	public static float[][] pos171(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z + 2f / 3f }, { -x + y, -x, z + 1f / 3f }, { -x, -y, z },
				{ y, -x + y, z + 2f / 3f }, { x - y, x, z + 1f / 3f }, };
	}

	public static float[][] pos172(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z + 1f / 3f }, { -x + y, -x, z + 2f / 3f }, { -x, -y, z },
				{ y, -x + y, z + 1f / 3f }, { x - y, x, z + 2f / 3f }, };
	}

	public static float[][] pos173(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, z + 1f / 2f },
				{ y, -x + y, z + 1f / 2f }, { x - y, x, z + 1f / 2f }, };
	}

	public static float[][] pos174(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { x, y, -z }, { -y, x - y, -z },
				{ -x + y, -x, -z }, };
	}

	public static float[][] pos175(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, z }, { y, -x + y, z },
				{ x - y, x, z }, { -x, -y, -z }, { y, -x + y, -z }, { x - y, x, -z }, { x, y, -z }, { -y, x - y, -z },
				{ -x + y, -x, -z }, };
	}

	public static float[][] pos176(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, z + 1f / 2f },
				{ y, -x + y, z + 1f / 2f }, { x - y, x, z + 1f / 2f }, { -x, -y, -z }, { y, -x + y, -z },
				{ x - y, x, -z }, { x, y, -z + 1f / 2f }, { -y, x - y, -z + 1f / 2f }, { -x + y, -x, -z + 1f / 2f }, };
	}

	public static float[][] pos177(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, z }, { y, -x + y, z },
				{ x - y, x, z }, { y, x, -z }, { x - y, -y, -z }, { -x, -x + y, -z }, { -y, -x, -z }, { -x + y, y, -z },
				{ x, x - y, -z }, };
	}

	public static float[][] pos178(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z + 1f / 3f }, { -x + y, -x, z + 2f / 3f },
				{ -x, -y, z + 1f / 2f }, { y, -x + y, z + 5f / 6f }, { x - y, x, z + 1f / 6f }, { y, x, -z + 1f / 3f },
				{ x - y, -y, -z }, { -x, -x + y, -z + 2f / 3f }, { -y, -x, -z + 5f / 6f }, { -x + y, y, -z + 1f / 2f },
				{ x, x - y, -z + 1f / 6f }, };
	}

	public static float[][] pos179(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z + 2f / 3f }, { -x + y, -x, z + 1f / 3f },
				{ -x, -y, z + 1f / 2f }, { y, -x + y, z + 1f / 6f }, { x - y, x, z + 5f / 6f }, { y, x, -z + 2f / 3f },
				{ x - y, -y, -z }, { -x, -x + y, -z + 1f / 3f }, { -y, -x, -z + 1f / 6f }, { -x + y, y, -z + 1f / 2f },
				{ x, x - y, -z + 5f / 6f }, };
	}

	public static float[][] pos180(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z + 2f / 3f }, { -x + y, -x, z + 1f / 3f }, { -x, -y, z },
				{ y, -x + y, z + 2f / 3f }, { x - y, x, z + 1f / 3f }, { y, x, -z + 2f / 3f }, { x - y, -y, -z },
				{ -x, -x + y, -z + 1f / 3f }, { -y, -x, -z + 2f / 3f }, { -x + y, y, -z },
				{ x, x - y, -z + 1f / 3f }, };
	}

	public static float[][] pos181(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z + 1f / 3f }, { -x + y, -x, z + 2f / 3f }, { -x, -y, z },
				{ y, -x + y, z + 1f / 3f }, { x - y, x, z + 2f / 3f }, { y, x, -z + 1f / 3f }, { x - y, -y, -z },
				{ -x, -x + y, -z + 2f / 3f }, { -y, -x, -z + 1f / 3f }, { -x + y, y, -z },
				{ x, x - y, -z + 2f / 3f }, };
	}

	public static float[][] pos182(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, z + 1f / 2f },
				{ y, -x + y, z + 1f / 2f }, { x - y, x, z + 1f / 2f }, { y, x, -z }, { x - y, -y, -z },
				{ -x, -x + y, -z }, { -y, -x, -z + 1f / 2f }, { -x + y, y, -z + 1f / 2f },
				{ x, x - y, -z + 1f / 2f }, };
	}

	public static float[][] pos183(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, z }, { y, -x + y, z },
				{ x - y, x, z }, { -y, -x, z }, { -x + y, y, z }, { x, x - y, z }, { y, x, z }, { x - y, -y, z },
				{ -x, -x + y, z }, };
	}

	public static float[][] pos184(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, z }, { y, -x + y, z },
				{ x - y, x, z }, { -y, -x, z + 1f / 2f }, { -x + y, y, z + 1f / 2f }, { x, x - y, z + 1f / 2f },
				{ y, x, z + 1f / 2f }, { x - y, -y, z + 1f / 2f }, { -x, -x + y, z + 1f / 2f }, };
	}

	public static float[][] pos185(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, z + 1f / 2f },
				{ y, -x + y, z + 1f / 2f }, { x - y, x, z + 1f / 2f }, { -y, -x, z + 1f / 2f },
				{ -x + y, y, z + 1f / 2f }, { x, x - y, z + 1f / 2f }, { y, x, z }, { x - y, -y, z },
				{ -x, -x + y, z }, };
	}

	public static float[][] pos186(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, z + 1f / 2f },
				{ y, -x + y, z + 1f / 2f }, { x - y, x, z + 1f / 2f }, { -y, -x, z }, { -x + y, y, z }, { x, x - y, z },
				{ y, x, z + 1f / 2f }, { x - y, -y, z + 1f / 2f }, { -x, -x + y, z + 1f / 2f }, };
	}

	public static float[][] pos187(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { x, y, -z }, { -y, x - y, -z },
				{ -x + y, -x, -z }, { -y, -x, z }, { -x + y, y, z }, { x, x - y, z }, { -y, -x, -z }, { -x + y, y, -z },
				{ x, x - y, -z }, };
	}

	public static float[][] pos188(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { x, y, -z + 1f / 2f },
				{ -y, x - y, -z + 1f / 2f }, { -x + y, -x, -z + 1f / 2f }, { -y, -x, z + 1f / 2f },
				{ -x + y, y, z + 1f / 2f }, { x, x - y, z + 1f / 2f }, { -y, -x, -z }, { -x + y, y, -z },
				{ x, x - y, -z }, };
	}

	public static float[][] pos189(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { x, y, -z }, { -y, x - y, -z },
				{ -x + y, -x, -z }, { y, x, -z }, { x - y, -y, -z }, { -x, -x + y, -z }, { y, x, z }, { x - y, -y, z },
				{ -x, -x + y, z }, };
	}

	public static float[][] pos190(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { x, y, -z + 1f / 2f },
				{ -y, x - y, -z + 1f / 2f }, { -x + y, -x, -z + 1f / 2f }, { y, x, -z }, { x - y, -y, -z },
				{ -x, -x + y, -z }, { y, x, z + 1f / 2f }, { x - y, -y, z + 1f / 2f }, { -x, -x + y, z + 1f / 2f }, };
	}

	public static float[][] pos191(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, z }, { y, -x + y, z },
				{ x - y, x, z }, { y, x, -z }, { x - y, -y, -z }, { -x, -x + y, -z }, { -y, -x, -z }, { -x + y, y, -z },
				{ x, x - y, -z }, { -x, -y, -z }, { y, -x + y, -z }, { x - y, x, -z }, { x, y, -z }, { -y, x - y, -z },
				{ -x + y, -x, -z }, { -y, -x, z }, { -x + y, y, z }, { x, x - y, z }, { y, x, z }, { x - y, -y, z },
				{ -x, -x + y, z }, };
	}

	public static float[][] pos192(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, z }, { y, -x + y, z },
				{ x - y, x, z }, { y, x, -z + 1f / 2f }, { x - y, -y, -z + 1f / 2f }, { -x, -x + y, -z + 1f / 2f },
				{ -y, -x, -z + 1f / 2f }, { -x + y, y, -z + 1f / 2f }, { x, x - y, -z + 1f / 2f }, { -x, -y, -z },
				{ y, -x + y, -z }, { x - y, x, -z }, { x, y, -z }, { -y, x - y, -z }, { -x + y, -x, -z },
				{ -y, -x, z + 1f / 2f }, { -x + y, y, z + 1f / 2f }, { x, x - y, z + 1f / 2f }, { y, x, z + 1f / 2f },
				{ x - y, -y, z + 1f / 2f }, { -x, -x + y, z + 1f / 2f }, };
	}

	public static float[][] pos193(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, z + 1f / 2f },
				{ y, -x + y, z + 1f / 2f }, { x - y, x, z + 1f / 2f }, { y, x, -z + 1f / 2f },
				{ x - y, -y, -z + 1f / 2f }, { -x, -x + y, -z + 1f / 2f }, { -y, -x, -z }, { -x + y, y, -z },
				{ x, x - y, -z }, { -x, -y, -z }, { y, -x + y, -z }, { x - y, x, -z }, { x, y, -z + 1f / 2f },
				{ -y, x - y, -z + 1f / 2f }, { -x + y, -x, -z + 1f / 2f }, { -y, -x, z + 1f / 2f },
				{ -x + y, y, z + 1f / 2f }, { x, x - y, z + 1f / 2f }, { y, x, z }, { x - y, -y, z },
				{ -x, -x + y, z }, };
	}

	public static float[][] pos194(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -y, x - y, z }, { -x + y, -x, z }, { -x, -y, z + 1f / 2f },
				{ y, -x + y, z + 1f / 2f }, { x - y, x, z + 1f / 2f }, { y, x, -z }, { x - y, -y, -z },
				{ -x, -x + y, -z }, { -y, -x, -z + 1f / 2f }, { -x + y, y, -z + 1f / 2f }, { x, x - y, -z + 1f / 2f },
				{ -x, -y, -z }, { y, -x + y, -z }, { x - y, x, -z }, { x, y, -z + 1f / 2f },
				{ -y, x - y, -z + 1f / 2f }, { -x + y, -x, -z + 1f / 2f }, { -y, -x, z }, { -x + y, y, z },
				{ x, x - y, z }, { y, x, z + 1f / 2f }, { x - y, -y, z + 1f / 2f }, { -x, -x + y, z + 1f / 2f }, };
	}

	public static float[][] pos195(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x }, };
	}

	public static float[][] pos196(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x, -y + 1f / 2f, -z + 1f / 2f }, { z, x + 1f / 2f, y + 1f / 2f }, { z, -x + 1f / 2f, -y + 1f / 2f },
				{ -z, -x + 1f / 2f, y + 1f / 2f }, { -z, x + 1f / 2f, -y + 1f / 2f }, { y, z + 1f / 2f, x + 1f / 2f },
				{ -y, z + 1f / 2f, -x + 1f / 2f }, { y, -z + 1f / 2f, -x + 1f / 2f }, { -y, -z + 1f / 2f, x + 1f / 2f },
				{ x + 1f / 2f, y, z + 1f / 2f }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x + 1f / 2f, y, -z + 1f / 2f },
				{ x + 1f / 2f, -y, -z + 1f / 2f }, { z + 1f / 2f, x, y + 1f / 2f }, { z + 1f / 2f, -x, -y + 1f / 2f },
				{ -z + 1f / 2f, -x, y + 1f / 2f }, { -z + 1f / 2f, x, -y + 1f / 2f }, { y + 1f / 2f, z, x + 1f / 2f },
				{ -y + 1f / 2f, z, -x + 1f / 2f }, { y + 1f / 2f, -z, -x + 1f / 2f }, { -y + 1f / 2f, -z, x + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z + 1f / 2f, x + 1f / 2f, y }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z + 1f / 2f, -x + 1f / 2f, y }, { -z + 1f / 2f, x + 1f / 2f, -y }, { y + 1f / 2f, z + 1f / 2f, x },
				{ -y + 1f / 2f, z + 1f / 2f, -x }, { y + 1f / 2f, -z + 1f / 2f, -x },
				{ -y + 1f / 2f, -z + 1f / 2f, x }, };
	}

	public static float[][] pos197(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ z + 1f / 2f, x + 1f / 2f, y + 1f / 2f }, { z + 1f / 2f, -x + 1f / 2f, -y + 1f / 2f },
				{ -z + 1f / 2f, -x + 1f / 2f, y + 1f / 2f }, { -z + 1f / 2f, x + 1f / 2f, -y + 1f / 2f },
				{ y + 1f / 2f, z + 1f / 2f, x + 1f / 2f }, { -y + 1f / 2f, z + 1f / 2f, -x + 1f / 2f },
				{ y + 1f / 2f, -z + 1f / 2f, -x + 1f / 2f }, { -y + 1f / 2f, -z + 1f / 2f, x + 1f / 2f }, };
	}

	public static float[][] pos198(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z, x, y }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z + 1f / 2f, -x, y + 1f / 2f }, { -z, x + 1f / 2f, -y + 1f / 2f }, { y, z, x },
				{ -y, z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, -x },
				{ -y + 1f / 2f, -z, x + 1f / 2f }, };
	}

	public static float[][] pos199(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z, x, y }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z + 1f / 2f, -x, y + 1f / 2f }, { -z, x + 1f / 2f, -y + 1f / 2f }, { y, z, x },
				{ -y, z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y + 1f / 2f, -z, x + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z }, { -x + 1f / 2f, y, -z },
				{ x, -y, -z + 1f / 2f }, { z + 1f / 2f, x + 1f / 2f, y + 1f / 2f }, { z, -x, -y + 1f / 2f },
				{ -z, -x + 1f / 2f, y }, { -z + 1f / 2f, x, -y }, { y + 1f / 2f, z + 1f / 2f, x + 1f / 2f },
				{ -y + 1f / 2f, z, -x }, { y, -z, -x + 1f / 2f }, { -y, -z + 1f / 2f, x }, };
	}

	public static float[][] pos200(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x }, { -x, -y, -z },
				{ x, y, -z }, { x, -y, z }, { -x, y, z }, { -z, -x, -y }, { -z, x, y }, { z, x, -y }, { z, -x, y },
				{ -y, -z, -x }, { y, -z, x }, { -y, z, x }, { y, z, -x }, };
	}

	public static float[][] pos201(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -z + 1f / 2f, -x + 1f / 2f, -y + 1f / 2f }, { -z + 1f / 2f, x + 1f / 2f, y + 1f / 2f },
				{ z + 1f / 2f, x + 1f / 2f, -y + 1f / 2f }, { z + 1f / 2f, -x + 1f / 2f, y + 1f / 2f },
				{ -y + 1f / 2f, -z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, x + 1f / 2f },
				{ -y + 1f / 2f, z + 1f / 2f, x + 1f / 2f }, { y + 1f / 2f, z + 1f / 2f, -x + 1f / 2f }, };
	}

	public static float[][] pos201alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y, -z + 1f / 2f },
				{ x, -y + 1f / 2f, -z + 1f / 2f }, { z, x, y }, { z, -x + 1f / 2f, -y + 1f / 2f },
				{ -z + 1f / 2f, -x + 1f / 2f, y }, { -z + 1f / 2f, x, -y + 1f / 2f }, { y, z, x },
				{ -y + 1f / 2f, z, -x + 1f / 2f }, { y, -z + 1f / 2f, -x + 1f / 2f }, { -y + 1f / 2f, -z + 1f / 2f, x },
				{ -x, -y, -z }, { x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y, z + 1f / 2f },
				{ -x, y + 1f / 2f, z + 1f / 2f }, { -z, -x, -y }, { -z, x + 1f / 2f, y + 1f / 2f },
				{ z + 1f / 2f, x + 1f / 2f, -y }, { z + 1f / 2f, -x, y + 1f / 2f }, { -y, -z, -x },
				{ y + 1f / 2f, -z, x + 1f / 2f }, { -y, z + 1f / 2f, x + 1f / 2f }, { y + 1f / 2f, z + 1f / 2f, -x }, };
	}

	public static float[][] pos202(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x }, { -x, -y, -z },
				{ x, y, -z }, { x, -y, z }, { -x, y, z }, { -z, -x, -y }, { -z, x, y }, { z, x, -y }, { z, -x, y },
				{ -y, -z, -x }, { y, -z, x }, { -y, z, x }, { y, z, -x }, { x, y + 1f / 2f, z + 1f / 2f },
				{ -x, -y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f }, { x, -y + 1f / 2f, -z + 1f / 2f },
				{ z, x + 1f / 2f, y + 1f / 2f }, { z, -x + 1f / 2f, -y + 1f / 2f }, { -z, -x + 1f / 2f, y + 1f / 2f },
				{ -z, x + 1f / 2f, -y + 1f / 2f }, { y, z + 1f / 2f, x + 1f / 2f }, { -y, z + 1f / 2f, -x + 1f / 2f },
				{ y, -z + 1f / 2f, -x + 1f / 2f }, { -y, -z + 1f / 2f, x + 1f / 2f },
				{ -x, -y + 1f / 2f, -z + 1f / 2f }, { x, y + 1f / 2f, -z + 1f / 2f }, { x, -y + 1f / 2f, z + 1f / 2f },
				{ -x, y + 1f / 2f, z + 1f / 2f }, { -z, -x + 1f / 2f, -y + 1f / 2f }, { -z, x + 1f / 2f, y + 1f / 2f },
				{ z, x + 1f / 2f, -y + 1f / 2f }, { z, -x + 1f / 2f, y + 1f / 2f }, { -y, -z + 1f / 2f, -x + 1f / 2f },
				{ y, -z + 1f / 2f, x + 1f / 2f }, { -y, z + 1f / 2f, x + 1f / 2f }, { y, z + 1f / 2f, -x + 1f / 2f },
				{ x + 1f / 2f, y, z + 1f / 2f }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x + 1f / 2f, y, -z + 1f / 2f },
				{ x + 1f / 2f, -y, -z + 1f / 2f }, { z + 1f / 2f, x, y + 1f / 2f }, { z + 1f / 2f, -x, -y + 1f / 2f },
				{ -z + 1f / 2f, -x, y + 1f / 2f }, { -z + 1f / 2f, x, -y + 1f / 2f }, { y + 1f / 2f, z, x + 1f / 2f },
				{ -y + 1f / 2f, z, -x + 1f / 2f }, { y + 1f / 2f, -z, -x + 1f / 2f }, { -y + 1f / 2f, -z, x + 1f / 2f },
				{ -x + 1f / 2f, -y, -z + 1f / 2f }, { x + 1f / 2f, y, -z + 1f / 2f }, { x + 1f / 2f, -y, z + 1f / 2f },
				{ -x + 1f / 2f, y, z + 1f / 2f }, { -z + 1f / 2f, -x, -y + 1f / 2f }, { -z + 1f / 2f, x, y + 1f / 2f },
				{ z + 1f / 2f, x, -y + 1f / 2f }, { z + 1f / 2f, -x, y + 1f / 2f }, { -y + 1f / 2f, -z, -x + 1f / 2f },
				{ y + 1f / 2f, -z, x + 1f / 2f }, { -y + 1f / 2f, z, x + 1f / 2f }, { y + 1f / 2f, z, -x + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z + 1f / 2f, x + 1f / 2f, y }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z + 1f / 2f, -x + 1f / 2f, y }, { -z + 1f / 2f, x + 1f / 2f, -y }, { y + 1f / 2f, z + 1f / 2f, x },
				{ -y + 1f / 2f, z + 1f / 2f, -x }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y + 1f / 2f, -z + 1f / 2f, x },
				{ -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z }, { -z + 1f / 2f, -x + 1f / 2f, -y }, { -z + 1f / 2f, x + 1f / 2f, y },
				{ z + 1f / 2f, x + 1f / 2f, -y }, { z + 1f / 2f, -x + 1f / 2f, y }, { -y + 1f / 2f, -z + 1f / 2f, -x },
				{ y + 1f / 2f, -z + 1f / 2f, x }, { -y + 1f / 2f, z + 1f / 2f, x }, { y + 1f / 2f, z + 1f / 2f, -x }, };
	}

	public static float[][] pos203(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x },
				{ -x + 1f / 4f, -y + 1f / 4f, -z + 1f / 4f }, { x + 1f / 4f, y + 1f / 4f, -z + 1f / 4f },
				{ x + 1f / 4f, -y + 1f / 4f, z + 1f / 4f }, { -x + 1f / 4f, y + 1f / 4f, z + 1f / 4f },
				{ -z + 1f / 4f, -x + 1f / 4f, -y + 1f / 4f }, { -z + 1f / 4f, x + 1f / 4f, y + 1f / 4f },
				{ z + 1f / 4f, x + 1f / 4f, -y + 1f / 4f }, { z + 1f / 4f, -x + 1f / 4f, y + 1f / 4f },
				{ -y + 1f / 4f, -z + 1f / 4f, -x + 1f / 4f }, { y + 1f / 4f, -z + 1f / 4f, x + 1f / 4f },
				{ -y + 1f / 4f, z + 1f / 4f, x + 1f / 4f }, { y + 1f / 4f, z + 1f / 4f, -x + 1f / 4f },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x, -y + 1f / 2f, -z + 1f / 2f }, { z, x + 1f / 2f, y + 1f / 2f }, { z, -x + 1f / 2f, -y + 1f / 2f },
				{ -z, -x + 1f / 2f, y + 1f / 2f }, { -z, x + 1f / 2f, -y + 1f / 2f }, { y, z + 1f / 2f, x + 1f / 2f },
				{ -y, z + 1f / 2f, -x + 1f / 2f }, { y, -z + 1f / 2f, -x + 1f / 2f }, { -y, -z + 1f / 2f, x + 1f / 2f },
				{ -x + 1f / 4f, -y + 3f / 4f, -z + 3f / 4f }, { x + 1f / 4f, y + 3f / 4f, -z + 3f / 4f },
				{ x + 1f / 4f, -y + 3f / 4f, z + 3f / 4f }, { -x + 1f / 4f, y + 3f / 4f, z + 3f / 4f },
				{ -z + 1f / 4f, -x + 3f / 4f, -y + 3f / 4f }, { -z + 1f / 4f, x + 3f / 4f, y + 3f / 4f },
				{ z + 1f / 4f, x + 3f / 4f, -y + 3f / 4f }, { z + 1f / 4f, -x + 3f / 4f, y + 3f / 4f },
				{ -y + 1f / 4f, -z + 3f / 4f, -x + 3f / 4f }, { y + 1f / 4f, -z + 3f / 4f, x + 3f / 4f },
				{ -y + 1f / 4f, z + 3f / 4f, x + 3f / 4f }, { y + 1f / 4f, z + 3f / 4f, -x + 3f / 4f },
				{ x + 1f / 2f, y, z + 1f / 2f }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x + 1f / 2f, y, -z + 1f / 2f },
				{ x + 1f / 2f, -y, -z + 1f / 2f }, { z + 1f / 2f, x, y + 1f / 2f }, { z + 1f / 2f, -x, -y + 1f / 2f },
				{ -z + 1f / 2f, -x, y + 1f / 2f }, { -z + 1f / 2f, x, -y + 1f / 2f }, { y + 1f / 2f, z, x + 1f / 2f },
				{ -y + 1f / 2f, z, -x + 1f / 2f }, { y + 1f / 2f, -z, -x + 1f / 2f }, { -y + 1f / 2f, -z, x + 1f / 2f },
				{ -x + 3f / 4f, -y + 1f / 4f, -z + 3f / 4f }, { x + 3f / 4f, y + 1f / 4f, -z + 3f / 4f },
				{ x + 3f / 4f, -y + 1f / 4f, z + 3f / 4f }, { -x + 3f / 4f, y + 1f / 4f, z + 3f / 4f },
				{ -z + 3f / 4f, -x + 1f / 4f, -y + 3f / 4f }, { -z + 3f / 4f, x + 1f / 4f, y + 3f / 4f },
				{ z + 3f / 4f, x + 1f / 4f, -y + 3f / 4f }, { z + 3f / 4f, -x + 1f / 4f, y + 3f / 4f },
				{ -y + 3f / 4f, -z + 1f / 4f, -x + 3f / 4f }, { y + 3f / 4f, -z + 1f / 4f, x + 3f / 4f },
				{ -y + 3f / 4f, z + 1f / 4f, x + 3f / 4f }, { y + 3f / 4f, z + 1f / 4f, -x + 3f / 4f },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z + 1f / 2f, x + 1f / 2f, y }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z + 1f / 2f, -x + 1f / 2f, y }, { -z + 1f / 2f, x + 1f / 2f, -y }, { y + 1f / 2f, z + 1f / 2f, x },
				{ -y + 1f / 2f, z + 1f / 2f, -x }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y + 1f / 2f, -z + 1f / 2f, x },
				{ -x + 3f / 4f, -y + 3f / 4f, -z + 1f / 4f }, { x + 3f / 4f, y + 3f / 4f, -z + 1f / 4f },
				{ x + 3f / 4f, -y + 3f / 4f, z + 1f / 4f }, { -x + 3f / 4f, y + 3f / 4f, z + 1f / 4f },
				{ -z + 3f / 4f, -x + 3f / 4f, -y + 1f / 4f }, { -z + 3f / 4f, x + 3f / 4f, y + 1f / 4f },
				{ z + 3f / 4f, x + 3f / 4f, -y + 1f / 4f }, { z + 3f / 4f, -x + 3f / 4f, y + 1f / 4f },
				{ -y + 3f / 4f, -z + 3f / 4f, -x + 1f / 4f }, { y + 3f / 4f, -z + 3f / 4f, x + 1f / 4f },
				{ -y + 3f / 4f, z + 3f / 4f, x + 1f / 4f }, { y + 3f / 4f, z + 3f / 4f, -x + 1f / 4f }, };
	}

	public static float[][] pos203alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 4f, -y + 1f / 4f, z }, { -x + 1f / 4f, y, -z + 1f / 4f },
				{ x, -y + 1f / 4f, -z + 1f / 4f }, { z, x, y }, { z, -x + 1f / 4f, -y + 1f / 4f },
				{ -z + 1f / 4f, -x + 1f / 4f, y }, { -z + 1f / 4f, x, -y + 1f / 4f }, { y, z, x },
				{ -y + 1f / 4f, z, -x + 1f / 4f }, { y, -z + 1f / 4f, -x + 1f / 4f }, { -y + 1f / 4f, -z + 1f / 4f, x },
				{ -x, -y, -z }, { x + 3f / 4f, y + 3f / 4f, -z }, { x + 3f / 4f, -y, z + 3f / 4f },
				{ -x, y + 3f / 4f, z + 3f / 4f }, { -z, -x, -y }, { -z, x + 3f / 4f, y + 3f / 4f },
				{ z + 3f / 4f, x + 3f / 4f, -y }, { z + 3f / 4f, -x, y + 3f / 4f }, { -y, -z, -x },
				{ y + 3f / 4f, -z, x + 3f / 4f }, { -y, z + 3f / 4f, x + 3f / 4f }, { y + 3f / 4f, z + 3f / 4f, -x },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 4f, -y + 3f / 4f, z + 1f / 2f },
				{ -x + 1f / 4f, y + 1f / 2f, -z + 3f / 4f }, { x, -y + 3f / 4f, -z + 3f / 4f },
				{ z, x + 1f / 2f, y + 1f / 2f }, { z, -x + 3f / 4f, -y + 3f / 4f },
				{ -z + 1f / 4f, -x + 3f / 4f, y + 1f / 2f }, { -z + 1f / 4f, x + 1f / 2f, -y + 3f / 4f },
				{ y, z + 1f / 2f, x + 1f / 2f }, { -y + 1f / 4f, z + 1f / 2f, -x + 3f / 4f },
				{ y, -z + 3f / 4f, -x + 3f / 4f }, { -y + 1f / 4f, -z + 3f / 4f, x + 1f / 2f },
				{ -x, -y + 1f / 2f, -z + 1f / 2f }, { x + 3f / 4f, y + 1f / 4f, -z + 1f / 2f },
				{ x + 3f / 4f, -y + 1f / 2f, z + 1f / 4f }, { -x, y + 1f / 4f, z + 1f / 4f },
				{ -z, -x + 1f / 2f, -y + 1f / 2f }, { -z, x + 1f / 4f, y + 1f / 4f },
				{ z + 3f / 4f, x + 1f / 4f, -y + 1f / 2f }, { z + 3f / 4f, -x + 1f / 2f, y + 1f / 4f },
				{ -y, -z + 1f / 2f, -x + 1f / 2f }, { y + 3f / 4f, -z + 1f / 2f, x + 1f / 4f },
				{ -y, z + 1f / 4f, x + 1f / 4f }, { y + 3f / 4f, z + 1f / 4f, -x + 1f / 2f },
				{ x + 1f / 2f, y, z + 1f / 2f }, { -x + 3f / 4f, -y + 1f / 4f, z + 1f / 2f },
				{ -x + 3f / 4f, y, -z + 3f / 4f }, { x + 1f / 2f, -y + 1f / 4f, -z + 3f / 4f },
				{ z + 1f / 2f, x, y + 1f / 2f }, { z + 1f / 2f, -x + 1f / 4f, -y + 3f / 4f },
				{ -z + 3f / 4f, -x + 1f / 4f, y + 1f / 2f }, { -z + 3f / 4f, x, -y + 3f / 4f },
				{ y + 1f / 2f, z, x + 1f / 2f }, { -y + 3f / 4f, z, -x + 3f / 4f },
				{ y + 1f / 2f, -z + 1f / 4f, -x + 3f / 4f }, { -y + 3f / 4f, -z + 1f / 4f, x + 1f / 2f },
				{ -x + 1f / 2f, -y, -z + 1f / 2f }, { x + 1f / 4f, y + 3f / 4f, -z + 1f / 2f },
				{ x + 1f / 4f, -y, z + 1f / 4f }, { -x + 1f / 2f, y + 3f / 4f, z + 1f / 4f },
				{ -z + 1f / 2f, -x, -y + 1f / 2f }, { -z + 1f / 2f, x + 3f / 4f, y + 1f / 4f },
				{ z + 1f / 4f, x + 3f / 4f, -y + 1f / 2f }, { z + 1f / 4f, -x, y + 1f / 4f },
				{ -y + 1f / 2f, -z, -x + 1f / 2f }, { y + 1f / 4f, -z, x + 1f / 4f },
				{ -y + 1f / 2f, z + 3f / 4f, x + 1f / 4f }, { y + 1f / 4f, z + 3f / 4f, -x + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 3f / 4f, -y + 3f / 4f, z },
				{ -x + 3f / 4f, y + 1f / 2f, -z + 1f / 4f }, { x + 1f / 2f, -y + 3f / 4f, -z + 1f / 4f },
				{ z + 1f / 2f, x + 1f / 2f, y }, { z + 1f / 2f, -x + 3f / 4f, -y + 1f / 4f },
				{ -z + 3f / 4f, -x + 3f / 4f, y }, { -z + 3f / 4f, x + 1f / 2f, -y + 1f / 4f },
				{ y + 1f / 2f, z + 1f / 2f, x }, { -y + 3f / 4f, z + 1f / 2f, -x + 1f / 4f },
				{ y + 1f / 2f, -z + 3f / 4f, -x + 1f / 4f }, { -y + 3f / 4f, -z + 3f / 4f, x },
				{ -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 4f, y + 1f / 4f, -z },
				{ x + 1f / 4f, -y + 1f / 2f, z + 3f / 4f }, { -x + 1f / 2f, y + 1f / 4f, z + 3f / 4f },
				{ -z + 1f / 2f, -x + 1f / 2f, -y }, { -z + 1f / 2f, x + 1f / 4f, y + 3f / 4f },
				{ z + 1f / 4f, x + 1f / 4f, -y }, { z + 1f / 4f, -x + 1f / 2f, y + 3f / 4f },
				{ -y + 1f / 2f, -z + 1f / 2f, -x }, { y + 1f / 4f, -z + 1f / 2f, x + 3f / 4f },
				{ -y + 1f / 2f, z + 1f / 4f, x + 3f / 4f }, { y + 1f / 4f, z + 1f / 4f, -x }, };
	}

	public static float[][] pos204(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x }, { -x, -y, -z },
				{ x, y, -z }, { x, -y, z }, { -x, y, z }, { -z, -x, -y }, { -z, x, y }, { z, x, -y }, { z, -x, y },
				{ -y, -z, -x }, { y, -z, x }, { -y, z, x }, { y, z, -x }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { z + 1f / 2f, x + 1f / 2f, y + 1f / 2f },
				{ z + 1f / 2f, -x + 1f / 2f, -y + 1f / 2f }, { -z + 1f / 2f, -x + 1f / 2f, y + 1f / 2f },
				{ -z + 1f / 2f, x + 1f / 2f, -y + 1f / 2f }, { y + 1f / 2f, z + 1f / 2f, x + 1f / 2f },
				{ -y + 1f / 2f, z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, -x + 1f / 2f },
				{ -y + 1f / 2f, -z + 1f / 2f, x + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -z + 1f / 2f, -x + 1f / 2f, -y + 1f / 2f },
				{ -z + 1f / 2f, x + 1f / 2f, y + 1f / 2f }, { z + 1f / 2f, x + 1f / 2f, -y + 1f / 2f },
				{ z + 1f / 2f, -x + 1f / 2f, y + 1f / 2f }, { -y + 1f / 2f, -z + 1f / 2f, -x + 1f / 2f },
				{ y + 1f / 2f, -z + 1f / 2f, x + 1f / 2f }, { -y + 1f / 2f, z + 1f / 2f, x + 1f / 2f },
				{ y + 1f / 2f, z + 1f / 2f, -x + 1f / 2f }, };
	}

	public static float[][] pos205(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z, x, y }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z + 1f / 2f, -x, y + 1f / 2f }, { -z, x + 1f / 2f, -y + 1f / 2f }, { y, z, x },
				{ -y, z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y + 1f / 2f, -z, x + 1f / 2f },
				{ -x, -y, -z }, { x + 1f / 2f, y, -z + 1f / 2f }, { x, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z }, { -z, -x, -y }, { -z + 1f / 2f, x + 1f / 2f, y },
				{ z + 1f / 2f, x, -y + 1f / 2f }, { z, -x + 1f / 2f, y + 1f / 2f }, { -y, -z, -x },
				{ y, -z + 1f / 2f, x + 1f / 2f }, { -y + 1f / 2f, z + 1f / 2f, x }, { y + 1f / 2f, z, -x + 1f / 2f }, };
	}

	public static float[][] pos206(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z, x, y }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z + 1f / 2f, -x, y + 1f / 2f }, { -z, x + 1f / 2f, -y + 1f / 2f }, { y, z, x },
				{ -y, z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y + 1f / 2f, -z, x + 1f / 2f },
				{ -x, -y, -z }, { x + 1f / 2f, y, -z + 1f / 2f }, { x, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z }, { -z, -x, -y }, { -z + 1f / 2f, x + 1f / 2f, y },
				{ z + 1f / 2f, x, -y + 1f / 2f }, { z, -x + 1f / 2f, y + 1f / 2f }, { -y, -z, -x },
				{ y, -z + 1f / 2f, x + 1f / 2f }, { -y + 1f / 2f, z + 1f / 2f, x }, { y + 1f / 2f, z, -x + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z }, { -x + 1f / 2f, y, -z },
				{ x, -y, -z + 1f / 2f }, { z + 1f / 2f, x + 1f / 2f, y + 1f / 2f }, { z, -x, -y + 1f / 2f },
				{ -z, -x + 1f / 2f, y }, { -z + 1f / 2f, x, -y }, { y + 1f / 2f, z + 1f / 2f, x + 1f / 2f },
				{ -y + 1f / 2f, z, -x }, { y, -z, -x + 1f / 2f }, { -y, -z + 1f / 2f, x },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x, y + 1f / 2f, -z }, { x + 1f / 2f, -y, z },
				{ -x, y, z + 1f / 2f }, { -z + 1f / 2f, -x + 1f / 2f, -y + 1f / 2f }, { -z, x, y + 1f / 2f },
				{ z, x + 1f / 2f, -y }, { z + 1f / 2f, -x, y }, { -y + 1f / 2f, -z + 1f / 2f, -x + 1f / 2f },
				{ y + 1f / 2f, -z, x }, { -y, z, x + 1f / 2f }, { y, z + 1f / 2f, -x }, };
	}

	public static float[][] pos207(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x }, { y, x, -z },
				{ -y, -x, -z }, { y, -x, z }, { -y, x, z }, { x, z, -y }, { -x, z, y }, { -x, -z, -y }, { x, -z, y },
				{ z, y, -x }, { z, -y, x }, { -z, y, x }, { -z, -y, -x }, };
	}

	public static float[][] pos208(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x },
				{ y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, z + 1f / 2f, -y + 1f / 2f }, { -x + 1f / 2f, z + 1f / 2f, y + 1f / 2f },
				{ -x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f }, { x + 1f / 2f, -z + 1f / 2f, y + 1f / 2f },
				{ z + 1f / 2f, y + 1f / 2f, -x + 1f / 2f }, { z + 1f / 2f, -y + 1f / 2f, x + 1f / 2f },
				{ -z + 1f / 2f, y + 1f / 2f, x + 1f / 2f }, { -z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f }, };
	}

	public static float[][] pos209(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x }, { y, x, -z },
				{ -y, -x, -z }, { y, -x, z }, { -y, x, z }, { x, z, -y }, { -x, z, y }, { -x, -z, -y }, { x, -z, y },
				{ z, y, -x }, { z, -y, x }, { -z, y, x }, { -z, -y, -x }, { x, y + 1f / 2f, z + 1f / 2f },
				{ -x, -y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f }, { x, -y + 1f / 2f, -z + 1f / 2f },
				{ z, x + 1f / 2f, y + 1f / 2f }, { z, -x + 1f / 2f, -y + 1f / 2f }, { -z, -x + 1f / 2f, y + 1f / 2f },
				{ -z, x + 1f / 2f, -y + 1f / 2f }, { y, z + 1f / 2f, x + 1f / 2f }, { -y, z + 1f / 2f, -x + 1f / 2f },
				{ y, -z + 1f / 2f, -x + 1f / 2f }, { -y, -z + 1f / 2f, x + 1f / 2f }, { y, x + 1f / 2f, -z + 1f / 2f },
				{ -y, -x + 1f / 2f, -z + 1f / 2f }, { y, -x + 1f / 2f, z + 1f / 2f }, { -y, x + 1f / 2f, z + 1f / 2f },
				{ x, z + 1f / 2f, -y + 1f / 2f }, { -x, z + 1f / 2f, y + 1f / 2f }, { -x, -z + 1f / 2f, -y + 1f / 2f },
				{ x, -z + 1f / 2f, y + 1f / 2f }, { z, y + 1f / 2f, -x + 1f / 2f }, { z, -y + 1f / 2f, x + 1f / 2f },
				{ -z, y + 1f / 2f, x + 1f / 2f }, { -z, -y + 1f / 2f, -x + 1f / 2f }, { x + 1f / 2f, y, z + 1f / 2f },
				{ -x + 1f / 2f, -y, z + 1f / 2f }, { -x + 1f / 2f, y, -z + 1f / 2f }, { x + 1f / 2f, -y, -z + 1f / 2f },
				{ z + 1f / 2f, x, y + 1f / 2f }, { z + 1f / 2f, -x, -y + 1f / 2f }, { -z + 1f / 2f, -x, y + 1f / 2f },
				{ -z + 1f / 2f, x, -y + 1f / 2f }, { y + 1f / 2f, z, x + 1f / 2f }, { -y + 1f / 2f, z, -x + 1f / 2f },
				{ y + 1f / 2f, -z, -x + 1f / 2f }, { -y + 1f / 2f, -z, x + 1f / 2f }, { y + 1f / 2f, x, -z + 1f / 2f },
				{ -y + 1f / 2f, -x, -z + 1f / 2f }, { y + 1f / 2f, -x, z + 1f / 2f }, { -y + 1f / 2f, x, z + 1f / 2f },
				{ x + 1f / 2f, z, -y + 1f / 2f }, { -x + 1f / 2f, z, y + 1f / 2f }, { -x + 1f / 2f, -z, -y + 1f / 2f },
				{ x + 1f / 2f, -z, y + 1f / 2f }, { z + 1f / 2f, y, -x + 1f / 2f }, { z + 1f / 2f, -y, x + 1f / 2f },
				{ -z + 1f / 2f, y, x + 1f / 2f }, { -z + 1f / 2f, -y, -x + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, z },
				{ -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, -z },
				{ z + 1f / 2f, x + 1f / 2f, y }, { z + 1f / 2f, -x + 1f / 2f, -y }, { -z + 1f / 2f, -x + 1f / 2f, y },
				{ -z + 1f / 2f, x + 1f / 2f, -y }, { y + 1f / 2f, z + 1f / 2f, x }, { -y + 1f / 2f, z + 1f / 2f, -x },
				{ y + 1f / 2f, -z + 1f / 2f, -x }, { -y + 1f / 2f, -z + 1f / 2f, x }, { y + 1f / 2f, x + 1f / 2f, -z },
				{ -y + 1f / 2f, -x + 1f / 2f, -z }, { y + 1f / 2f, -x + 1f / 2f, z }, { -y + 1f / 2f, x + 1f / 2f, z },
				{ x + 1f / 2f, z + 1f / 2f, -y }, { -x + 1f / 2f, z + 1f / 2f, y }, { -x + 1f / 2f, -z + 1f / 2f, -y },
				{ x + 1f / 2f, -z + 1f / 2f, y }, { z + 1f / 2f, y + 1f / 2f, -x }, { z + 1f / 2f, -y + 1f / 2f, x },
				{ -z + 1f / 2f, y + 1f / 2f, x }, { -z + 1f / 2f, -y + 1f / 2f, -x }, };
	}

	public static float[][] pos210(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y, -z + 1f / 2f }, { z, x, y }, { z + 1f / 2f, -x, -y + 1f / 2f },
				{ -z, -x + 1f / 2f, y + 1f / 2f }, { -z + 1f / 2f, x + 1f / 2f, -y }, { y, z, x },
				{ -y + 1f / 2f, z + 1f / 2f, -x }, { y + 1f / 2f, -z, -x + 1f / 2f }, { -y, -z + 1f / 2f, x + 1f / 2f },
				{ y + 3f / 4f, x + 1f / 4f, -z + 3f / 4f }, { -y + 1f / 4f, -x + 1f / 4f, -z + 1f / 4f },
				{ y + 1f / 4f, -x + 3f / 4f, z + 3f / 4f }, { -y + 3f / 4f, x + 3f / 4f, z + 1f / 4f },
				{ x + 3f / 4f, z + 1f / 4f, -y + 3f / 4f }, { -x + 3f / 4f, z + 3f / 4f, y + 1f / 4f },
				{ -x + 1f / 4f, -z + 1f / 4f, -y + 1f / 4f }, { x + 1f / 4f, -z + 3f / 4f, y + 3f / 4f },
				{ z + 3f / 4f, y + 1f / 4f, -x + 3f / 4f }, { z + 1f / 4f, -y + 3f / 4f, x + 3f / 4f },
				{ -z + 3f / 4f, y + 3f / 4f, x + 1f / 4f }, { -z + 1f / 4f, -y + 1f / 4f, -x + 1f / 4f },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x, -y, z }, { -x + 1f / 2f, y, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z, x + 1f / 2f, y + 1f / 2f }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z, -x, y }, { -z + 1f / 2f, x, -y + 1f / 2f }, { y, z + 1f / 2f, x + 1f / 2f },
				{ -y + 1f / 2f, z, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y, -z, x },
				{ y + 3f / 4f, x + 3f / 4f, -z + 1f / 4f }, { -y + 1f / 4f, -x + 3f / 4f, -z + 3f / 4f },
				{ y + 1f / 4f, -x + 1f / 4f, z + 1f / 4f }, { -y + 3f / 4f, x + 1f / 4f, z + 3f / 4f },
				{ x + 3f / 4f, z + 3f / 4f, -y + 1f / 4f }, { -x + 3f / 4f, z + 1f / 4f, y + 3f / 4f },
				{ -x + 1f / 4f, -z + 3f / 4f, -y + 3f / 4f }, { x + 1f / 4f, -z + 1f / 4f, y + 1f / 4f },
				{ z + 3f / 4f, y + 3f / 4f, -x + 1f / 4f }, { z + 1f / 4f, -y + 1f / 4f, x + 1f / 4f },
				{ -z + 3f / 4f, y + 1f / 4f, x + 3f / 4f }, { -z + 1f / 4f, -y + 3f / 4f, -x + 3f / 4f },
				{ x + 1f / 2f, y, z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x, -y, -z }, { z + 1f / 2f, x, y + 1f / 2f }, { z, -x, -y }, { -z + 1f / 2f, -x + 1f / 2f, y },
				{ -z, x + 1f / 2f, -y + 1f / 2f }, { y + 1f / 2f, z, x + 1f / 2f }, { -y, z + 1f / 2f, -x + 1f / 2f },
				{ y, -z, -x }, { -y + 1f / 2f, -z + 1f / 2f, x }, { y + 1f / 4f, x + 1f / 4f, -z + 1f / 4f },
				{ -y + 3f / 4f, -x + 1f / 4f, -z + 3f / 4f }, { y + 3f / 4f, -x + 3f / 4f, z + 1f / 4f },
				{ -y + 1f / 4f, x + 3f / 4f, z + 3f / 4f }, { x + 1f / 4f, z + 1f / 4f, -y + 1f / 4f },
				{ -x + 1f / 4f, z + 3f / 4f, y + 3f / 4f }, { -x + 3f / 4f, -z + 1f / 4f, -y + 3f / 4f },
				{ x + 3f / 4f, -z + 3f / 4f, y + 1f / 4f }, { z + 1f / 4f, y + 1f / 4f, -x + 1f / 4f },
				{ z + 3f / 4f, -y + 3f / 4f, x + 1f / 4f }, { -z + 1f / 4f, y + 3f / 4f, x + 3f / 4f },
				{ -z + 3f / 4f, -y + 1f / 4f, -x + 3f / 4f }, { x + 1f / 2f, y + 1f / 2f, z },
				{ -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y, -z }, { x, -y + 1f / 2f, -z + 1f / 2f },
				{ z + 1f / 2f, x + 1f / 2f, y }, { z, -x + 1f / 2f, -y + 1f / 2f }, { -z + 1f / 2f, -x, y + 1f / 2f },
				{ -z, x, -y }, { y + 1f / 2f, z + 1f / 2f, x }, { -y, z, -x }, { y, -z + 1f / 2f, -x + 1f / 2f },
				{ -y + 1f / 2f, -z, x + 1f / 2f }, { y + 1f / 4f, x + 3f / 4f, -z + 3f / 4f },
				{ -y + 3f / 4f, -x + 3f / 4f, -z + 1f / 4f }, { y + 3f / 4f, -x + 1f / 4f, z + 3f / 4f },
				{ -y + 1f / 4f, x + 1f / 4f, z + 1f / 4f }, { x + 1f / 4f, z + 3f / 4f, -y + 3f / 4f },
				{ -x + 1f / 4f, z + 1f / 4f, y + 1f / 4f }, { -x + 3f / 4f, -z + 3f / 4f, -y + 1f / 4f },
				{ x + 3f / 4f, -z + 1f / 4f, y + 3f / 4f }, { z + 1f / 4f, y + 3f / 4f, -x + 3f / 4f },
				{ z + 3f / 4f, -y + 1f / 4f, x + 3f / 4f }, { -z + 1f / 4f, y + 1f / 4f, x + 1f / 4f },
				{ -z + 3f / 4f, -y + 3f / 4f, -x + 1f / 4f }, };
	}

	public static float[][] pos211(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x }, { y, x, -z },
				{ -y, -x, -z }, { y, -x, z }, { -y, x, z }, { x, z, -y }, { -x, z, y }, { -x, -z, -y }, { x, -z, y },
				{ z, y, -x }, { z, -y, x }, { -z, y, x }, { -z, -y, -x }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { z + 1f / 2f, x + 1f / 2f, y + 1f / 2f },
				{ z + 1f / 2f, -x + 1f / 2f, -y + 1f / 2f }, { -z + 1f / 2f, -x + 1f / 2f, y + 1f / 2f },
				{ -z + 1f / 2f, x + 1f / 2f, -y + 1f / 2f }, { y + 1f / 2f, z + 1f / 2f, x + 1f / 2f },
				{ -y + 1f / 2f, z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, -x + 1f / 2f },
				{ -y + 1f / 2f, -z + 1f / 2f, x + 1f / 2f }, { y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f },
				{ -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f },
				{ -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, { x + 1f / 2f, z + 1f / 2f, -y + 1f / 2f },
				{ -x + 1f / 2f, z + 1f / 2f, y + 1f / 2f }, { -x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f },
				{ x + 1f / 2f, -z + 1f / 2f, y + 1f / 2f }, { z + 1f / 2f, y + 1f / 2f, -x + 1f / 2f },
				{ z + 1f / 2f, -y + 1f / 2f, x + 1f / 2f }, { -z + 1f / 2f, y + 1f / 2f, x + 1f / 2f },
				{ -z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f }, };
	}

	public static float[][] pos212(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z, x, y }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z + 1f / 2f, -x, y + 1f / 2f }, { -z, x + 1f / 2f, -y + 1f / 2f }, { y, z, x },
				{ -y, z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y + 1f / 2f, -z, x + 1f / 2f },
				{ y + 1f / 4f, x + 3f / 4f, -z + 3f / 4f }, { -y + 1f / 4f, -x + 1f / 4f, -z + 1f / 4f },
				{ y + 3f / 4f, -x + 3f / 4f, z + 1f / 4f }, { -y + 3f / 4f, x + 1f / 4f, z + 3f / 4f },
				{ x + 1f / 4f, z + 3f / 4f, -y + 3f / 4f }, { -x + 3f / 4f, z + 1f / 4f, y + 3f / 4f },
				{ -x + 1f / 4f, -z + 1f / 4f, -y + 1f / 4f }, { x + 3f / 4f, -z + 3f / 4f, y + 1f / 4f },
				{ z + 1f / 4f, y + 3f / 4f, -x + 3f / 4f }, { z + 3f / 4f, -y + 3f / 4f, x + 1f / 4f },
				{ -z + 3f / 4f, y + 1f / 4f, x + 3f / 4f }, { -z + 1f / 4f, -y + 1f / 4f, -x + 1f / 4f }, };
	}

	public static float[][] pos213(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z, x, y }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z + 1f / 2f, -x, y + 1f / 2f }, { -z, x + 1f / 2f, -y + 1f / 2f }, { y, z, x },
				{ -y, z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y + 1f / 2f, -z, x + 1f / 2f },
				{ y + 3f / 4f, x + 1f / 4f, -z + 1f / 4f }, { -y + 3f / 4f, -x + 3f / 4f, -z + 3f / 4f },
				{ y + 1f / 4f, -x + 1f / 4f, z + 3f / 4f }, { -y + 1f / 4f, x + 3f / 4f, z + 1f / 4f },
				{ x + 3f / 4f, z + 1f / 4f, -y + 1f / 4f }, { -x + 1f / 4f, z + 3f / 4f, y + 1f / 4f },
				{ -x + 3f / 4f, -z + 3f / 4f, -y + 3f / 4f }, { x + 1f / 4f, -z + 1f / 4f, y + 3f / 4f },
				{ z + 3f / 4f, y + 1f / 4f, -x + 1f / 4f }, { z + 1f / 4f, -y + 1f / 4f, x + 3f / 4f },
				{ -z + 1f / 4f, y + 3f / 4f, x + 1f / 4f }, { -z + 3f / 4f, -y + 3f / 4f, -x + 3f / 4f }, };
	}

	public static float[][] pos214(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z, x, y }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z + 1f / 2f, -x, y + 1f / 2f }, { -z, x + 1f / 2f, -y + 1f / 2f }, { y, z, x },
				{ -y, z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y + 1f / 2f, -z, x + 1f / 2f },
				{ y + 3f / 4f, x + 1f / 4f, -z + 1f / 4f }, { -y + 3f / 4f, -x + 3f / 4f, -z + 3f / 4f },
				{ y + 1f / 4f, -x + 1f / 4f, z + 3f / 4f }, { -y + 1f / 4f, x + 3f / 4f, z + 1f / 4f },
				{ x + 3f / 4f, z + 1f / 4f, -y + 1f / 4f }, { -x + 1f / 4f, z + 3f / 4f, y + 1f / 4f },
				{ -x + 3f / 4f, -z + 3f / 4f, -y + 3f / 4f }, { x + 1f / 4f, -z + 1f / 4f, y + 3f / 4f },
				{ z + 3f / 4f, y + 1f / 4f, -x + 1f / 4f }, { z + 1f / 4f, -y + 1f / 4f, x + 3f / 4f },
				{ -z + 1f / 4f, y + 3f / 4f, x + 1f / 4f }, { -z + 3f / 4f, -y + 3f / 4f, -x + 3f / 4f },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z }, { -x + 1f / 2f, y, -z },
				{ x, -y, -z + 1f / 2f }, { z + 1f / 2f, x + 1f / 2f, y + 1f / 2f }, { z, -x, -y + 1f / 2f },
				{ -z, -x + 1f / 2f, y }, { -z + 1f / 2f, x, -y }, { y + 1f / 2f, z + 1f / 2f, x + 1f / 2f },
				{ -y + 1f / 2f, z, -x }, { y, -z, -x + 1f / 2f }, { -y, -z + 1f / 2f, x },
				{ y + 1f / 4f, x + 3f / 4f, -z + 3f / 4f }, { -y + 1f / 4f, -x + 1f / 4f, -z + 1f / 4f },
				{ y + 3f / 4f, -x + 3f / 4f, z + 1f / 4f }, { -y + 3f / 4f, x + 1f / 4f, z + 3f / 4f },
				{ x + 1f / 4f, z + 3f / 4f, -y + 3f / 4f }, { -x + 3f / 4f, z + 1f / 4f, y + 3f / 4f },
				{ -x + 1f / 4f, -z + 1f / 4f, -y + 1f / 4f }, { x + 3f / 4f, -z + 3f / 4f, y + 1f / 4f },
				{ z + 1f / 4f, y + 3f / 4f, -x + 3f / 4f }, { z + 3f / 4f, -y + 3f / 4f, x + 1f / 4f },
				{ -z + 3f / 4f, y + 1f / 4f, x + 3f / 4f }, { -z + 1f / 4f, -y + 1f / 4f, -x + 1f / 4f }, };
	}

	public static float[][] pos215(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x }, { y, x, z },
				{ -y, -x, z }, { y, -x, -z }, { -y, x, -z }, { x, z, y }, { -x, z, -y }, { -x, -z, y }, { x, -z, -y },
				{ z, y, x }, { z, -y, -x }, { -z, y, -x }, { -z, -y, x }, };
	}

	public static float[][] pos216(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x }, { y, x, z },
				{ -y, -x, z }, { y, -x, -z }, { -y, x, -z }, { x, z, y }, { -x, z, -y }, { -x, -z, y }, { x, -z, -y },
				{ z, y, x }, { z, -y, -x }, { -z, y, -x }, { -z, -y, x }, { x, y + 1f / 2f, z + 1f / 2f },
				{ -x, -y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f }, { x, -y + 1f / 2f, -z + 1f / 2f },
				{ z, x + 1f / 2f, y + 1f / 2f }, { z, -x + 1f / 2f, -y + 1f / 2f }, { -z, -x + 1f / 2f, y + 1f / 2f },
				{ -z, x + 1f / 2f, -y + 1f / 2f }, { y, z + 1f / 2f, x + 1f / 2f }, { -y, z + 1f / 2f, -x + 1f / 2f },
				{ y, -z + 1f / 2f, -x + 1f / 2f }, { -y, -z + 1f / 2f, x + 1f / 2f }, { y, x + 1f / 2f, z + 1f / 2f },
				{ -y, -x + 1f / 2f, z + 1f / 2f }, { y, -x + 1f / 2f, -z + 1f / 2f }, { -y, x + 1f / 2f, -z + 1f / 2f },
				{ x, z + 1f / 2f, y + 1f / 2f }, { -x, z + 1f / 2f, -y + 1f / 2f }, { -x, -z + 1f / 2f, y + 1f / 2f },
				{ x, -z + 1f / 2f, -y + 1f / 2f }, { z, y + 1f / 2f, x + 1f / 2f }, { z, -y + 1f / 2f, -x + 1f / 2f },
				{ -z, y + 1f / 2f, -x + 1f / 2f }, { -z, -y + 1f / 2f, x + 1f / 2f }, { x + 1f / 2f, y, z + 1f / 2f },
				{ -x + 1f / 2f, -y, z + 1f / 2f }, { -x + 1f / 2f, y, -z + 1f / 2f }, { x + 1f / 2f, -y, -z + 1f / 2f },
				{ z + 1f / 2f, x, y + 1f / 2f }, { z + 1f / 2f, -x, -y + 1f / 2f }, { -z + 1f / 2f, -x, y + 1f / 2f },
				{ -z + 1f / 2f, x, -y + 1f / 2f }, { y + 1f / 2f, z, x + 1f / 2f }, { -y + 1f / 2f, z, -x + 1f / 2f },
				{ y + 1f / 2f, -z, -x + 1f / 2f }, { -y + 1f / 2f, -z, x + 1f / 2f }, { y + 1f / 2f, x, z + 1f / 2f },
				{ -y + 1f / 2f, -x, z + 1f / 2f }, { y + 1f / 2f, -x, -z + 1f / 2f }, { -y + 1f / 2f, x, -z + 1f / 2f },
				{ x + 1f / 2f, z, y + 1f / 2f }, { -x + 1f / 2f, z, -y + 1f / 2f }, { -x + 1f / 2f, -z, y + 1f / 2f },
				{ x + 1f / 2f, -z, -y + 1f / 2f }, { z + 1f / 2f, y, x + 1f / 2f }, { z + 1f / 2f, -y, -x + 1f / 2f },
				{ -z + 1f / 2f, y, -x + 1f / 2f }, { -z + 1f / 2f, -y, x + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, z },
				{ -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, -z },
				{ z + 1f / 2f, x + 1f / 2f, y }, { z + 1f / 2f, -x + 1f / 2f, -y }, { -z + 1f / 2f, -x + 1f / 2f, y },
				{ -z + 1f / 2f, x + 1f / 2f, -y }, { y + 1f / 2f, z + 1f / 2f, x }, { -y + 1f / 2f, z + 1f / 2f, -x },
				{ y + 1f / 2f, -z + 1f / 2f, -x }, { -y + 1f / 2f, -z + 1f / 2f, x }, { y + 1f / 2f, x + 1f / 2f, z },
				{ -y + 1f / 2f, -x + 1f / 2f, z }, { y + 1f / 2f, -x + 1f / 2f, -z }, { -y + 1f / 2f, x + 1f / 2f, -z },
				{ x + 1f / 2f, z + 1f / 2f, y }, { -x + 1f / 2f, z + 1f / 2f, -y }, { -x + 1f / 2f, -z + 1f / 2f, y },
				{ x + 1f / 2f, -z + 1f / 2f, -y }, { z + 1f / 2f, y + 1f / 2f, x }, { z + 1f / 2f, -y + 1f / 2f, -x },
				{ -z + 1f / 2f, y + 1f / 2f, -x }, { -z + 1f / 2f, -y + 1f / 2f, x }, };
	}

	public static float[][] pos217(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x }, { y, x, z },
				{ -y, -x, z }, { y, -x, -z }, { -y, x, -z }, { x, z, y }, { -x, z, -y }, { -x, -z, y }, { x, -z, -y },
				{ z, y, x }, { z, -y, -x }, { -z, y, -x }, { -z, -y, x }, { x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { z + 1f / 2f, x + 1f / 2f, y + 1f / 2f },
				{ z + 1f / 2f, -x + 1f / 2f, -y + 1f / 2f }, { -z + 1f / 2f, -x + 1f / 2f, y + 1f / 2f },
				{ -z + 1f / 2f, x + 1f / 2f, -y + 1f / 2f }, { y + 1f / 2f, z + 1f / 2f, x + 1f / 2f },
				{ -y + 1f / 2f, z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, -x + 1f / 2f },
				{ -y + 1f / 2f, -z + 1f / 2f, x + 1f / 2f }, { y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, z + 1f / 2f, y + 1f / 2f },
				{ -x + 1f / 2f, z + 1f / 2f, -y + 1f / 2f }, { -x + 1f / 2f, -z + 1f / 2f, y + 1f / 2f },
				{ x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f }, { z + 1f / 2f, y + 1f / 2f, x + 1f / 2f },
				{ z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f }, { -z + 1f / 2f, y + 1f / 2f, -x + 1f / 2f },
				{ -z + 1f / 2f, -y + 1f / 2f, x + 1f / 2f }, };
	}

	public static float[][] pos218(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x },
				{ y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, z + 1f / 2f, y + 1f / 2f }, { -x + 1f / 2f, z + 1f / 2f, -y + 1f / 2f },
				{ -x + 1f / 2f, -z + 1f / 2f, y + 1f / 2f }, { x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f },
				{ z + 1f / 2f, y + 1f / 2f, x + 1f / 2f }, { z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f },
				{ -z + 1f / 2f, y + 1f / 2f, -x + 1f / 2f }, { -z + 1f / 2f, -y + 1f / 2f, x + 1f / 2f }, };
	}

	public static float[][] pos219(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x },
				{ y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, z + 1f / 2f, y + 1f / 2f }, { -x + 1f / 2f, z + 1f / 2f, -y + 1f / 2f },
				{ -x + 1f / 2f, -z + 1f / 2f, y + 1f / 2f }, { x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f },
				{ z + 1f / 2f, y + 1f / 2f, x + 1f / 2f }, { z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f },
				{ -z + 1f / 2f, y + 1f / 2f, -x + 1f / 2f }, { -z + 1f / 2f, -y + 1f / 2f, x + 1f / 2f },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x, -y + 1f / 2f, -z + 1f / 2f }, { z, x + 1f / 2f, y + 1f / 2f }, { z, -x + 1f / 2f, -y + 1f / 2f },
				{ -z, -x + 1f / 2f, y + 1f / 2f }, { -z, x + 1f / 2f, -y + 1f / 2f }, { y, z + 1f / 2f, x + 1f / 2f },
				{ -y, z + 1f / 2f, -x + 1f / 2f }, { y, -z + 1f / 2f, -x + 1f / 2f }, { -y, -z + 1f / 2f, x + 1f / 2f },
				{ y + 1f / 2f, x, z }, { -y + 1f / 2f, -x, z }, { y + 1f / 2f, -x, -z }, { -y + 1f / 2f, x, -z },
				{ x + 1f / 2f, z, y }, { -x + 1f / 2f, z, -y }, { -x + 1f / 2f, -z, y }, { x + 1f / 2f, -z, -y },
				{ z + 1f / 2f, y, x }, { z + 1f / 2f, -y, -x }, { -z + 1f / 2f, y, -x }, { -z + 1f / 2f, -y, x },
				{ x + 1f / 2f, y, z + 1f / 2f }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x + 1f / 2f, y, -z + 1f / 2f },
				{ x + 1f / 2f, -y, -z + 1f / 2f }, { z + 1f / 2f, x, y + 1f / 2f }, { z + 1f / 2f, -x, -y + 1f / 2f },
				{ -z + 1f / 2f, -x, y + 1f / 2f }, { -z + 1f / 2f, x, -y + 1f / 2f }, { y + 1f / 2f, z, x + 1f / 2f },
				{ -y + 1f / 2f, z, -x + 1f / 2f }, { y + 1f / 2f, -z, -x + 1f / 2f }, { -y + 1f / 2f, -z, x + 1f / 2f },
				{ y, x + 1f / 2f, z }, { -y, -x + 1f / 2f, z }, { y, -x + 1f / 2f, -z }, { -y, x + 1f / 2f, -z },
				{ x, z + 1f / 2f, y }, { -x, z + 1f / 2f, -y }, { -x, -z + 1f / 2f, y }, { x, -z + 1f / 2f, -y },
				{ z, y + 1f / 2f, x }, { z, -y + 1f / 2f, -x }, { -z, y + 1f / 2f, -x }, { -z, -y + 1f / 2f, x },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z + 1f / 2f, x + 1f / 2f, y }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z + 1f / 2f, -x + 1f / 2f, y }, { -z + 1f / 2f, x + 1f / 2f, -y }, { y + 1f / 2f, z + 1f / 2f, x },
				{ -y + 1f / 2f, z + 1f / 2f, -x }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y + 1f / 2f, -z + 1f / 2f, x },
				{ y, x, z + 1f / 2f }, { -y, -x, z + 1f / 2f }, { y, -x, -z + 1f / 2f }, { -y, x, -z + 1f / 2f },
				{ x, z, y + 1f / 2f }, { -x, z, -y + 1f / 2f }, { -x, -z, y + 1f / 2f }, { x, -z, -y + 1f / 2f },
				{ z, y, x + 1f / 2f }, { z, -y, -x + 1f / 2f }, { -z, y, -x + 1f / 2f }, { -z, -y, x + 1f / 2f }, };
	}

	public static float[][] pos220(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z, x, y }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z + 1f / 2f, -x, y + 1f / 2f }, { -z, x + 1f / 2f, -y + 1f / 2f }, { y, z, x },
				{ -y, z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y + 1f / 2f, -z, x + 1f / 2f },
				{ y + 1f / 4f, x + 1f / 4f, z + 1f / 4f }, { -y + 1f / 4f, -x + 3f / 4f, z + 3f / 4f },
				{ y + 3f / 4f, -x + 1f / 4f, -z + 3f / 4f }, { -y + 3f / 4f, x + 3f / 4f, -z + 1f / 4f },
				{ x + 1f / 4f, z + 1f / 4f, y + 1f / 4f }, { -x + 3f / 4f, z + 3f / 4f, -y + 1f / 4f },
				{ -x + 1f / 4f, -z + 3f / 4f, y + 3f / 4f }, { x + 3f / 4f, -z + 1f / 4f, -y + 3f / 4f },
				{ z + 1f / 4f, y + 1f / 4f, x + 1f / 4f }, { z + 3f / 4f, -y + 1f / 4f, -x + 3f / 4f },
				{ -z + 3f / 4f, y + 3f / 4f, -x + 1f / 4f }, { -z + 1f / 4f, -y + 3f / 4f, x + 3f / 4f },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z }, { -x + 1f / 2f, y, -z },
				{ x, -y, -z + 1f / 2f }, { z + 1f / 2f, x + 1f / 2f, y + 1f / 2f }, { z, -x, -y + 1f / 2f },
				{ -z, -x + 1f / 2f, y }, { -z + 1f / 2f, x, -y }, { y + 1f / 2f, z + 1f / 2f, x + 1f / 2f },
				{ -y + 1f / 2f, z, -x }, { y, -z, -x + 1f / 2f }, { -y, -z + 1f / 2f, x },
				{ y + 3f / 4f, x + 3f / 4f, z + 3f / 4f }, { -y + 3f / 4f, -x + 1f / 4f, z + 1f / 4f },
				{ y + 1f / 4f, -x + 3f / 4f, -z + 1f / 4f }, { -y + 1f / 4f, x + 1f / 4f, -z + 3f / 4f },
				{ x + 3f / 4f, z + 3f / 4f, y + 3f / 4f }, { -x + 1f / 4f, z + 1f / 4f, -y + 3f / 4f },
				{ -x + 3f / 4f, -z + 1f / 4f, y + 1f / 4f }, { x + 1f / 4f, -z + 3f / 4f, -y + 1f / 4f },
				{ z + 3f / 4f, y + 3f / 4f, x + 3f / 4f }, { z + 1f / 4f, -y + 3f / 4f, -x + 1f / 4f },
				{ -z + 1f / 4f, y + 1f / 4f, -x + 3f / 4f }, { -z + 3f / 4f, -y + 1f / 4f, x + 1f / 4f }, };
	}

	public static float[][] pos221(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x }, { y, x, -z },
				{ -y, -x, -z }, { y, -x, z }, { -y, x, z }, { x, z, -y }, { -x, z, y }, { -x, -z, -y }, { x, -z, y },
				{ z, y, -x }, { z, -y, x }, { -z, y, x }, { -z, -y, -x }, { -x, -y, -z }, { x, y, -z }, { x, -y, z },
				{ -x, y, z }, { -z, -x, -y }, { -z, x, y }, { z, x, -y }, { z, -x, y }, { -y, -z, -x }, { y, -z, x },
				{ -y, z, x }, { y, z, -x }, { -y, -x, z }, { y, x, z }, { -y, x, -z }, { y, -x, -z }, { -x, -z, y },
				{ x, -z, -y }, { x, z, y }, { -x, z, -y }, { -z, -y, x }, { -z, y, -x }, { z, -y, -x }, { z, y, x }, };
	}

	public static float[][] pos222(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x }, { y, x, -z },
				{ -y, -x, -z }, { y, -x, z }, { -y, x, z }, { x, z, -y }, { -x, z, y }, { -x, -z, -y }, { x, -z, y },
				{ z, y, -x }, { z, -y, x }, { -z, y, x }, { -z, -y, -x }, { -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -z + 1f / 2f, -x + 1f / 2f, -y + 1f / 2f },
				{ -z + 1f / 2f, x + 1f / 2f, y + 1f / 2f }, { z + 1f / 2f, x + 1f / 2f, -y + 1f / 2f },
				{ z + 1f / 2f, -x + 1f / 2f, y + 1f / 2f }, { -y + 1f / 2f, -z + 1f / 2f, -x + 1f / 2f },
				{ y + 1f / 2f, -z + 1f / 2f, x + 1f / 2f }, { -y + 1f / 2f, z + 1f / 2f, x + 1f / 2f },
				{ y + 1f / 2f, z + 1f / 2f, -x + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { -x + 1f / 2f, -z + 1f / 2f, y + 1f / 2f },
				{ x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f }, { x + 1f / 2f, z + 1f / 2f, y + 1f / 2f },
				{ -x + 1f / 2f, z + 1f / 2f, -y + 1f / 2f }, { -z + 1f / 2f, -y + 1f / 2f, x + 1f / 2f },
				{ -z + 1f / 2f, y + 1f / 2f, -x + 1f / 2f }, { z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f },
				{ z + 1f / 2f, y + 1f / 2f, x + 1f / 2f }, };
	}

	public static float[][] pos222alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y, -z + 1f / 2f },
				{ x, -y + 1f / 2f, -z + 1f / 2f }, { z, x, y }, { z, -x + 1f / 2f, -y + 1f / 2f },
				{ -z + 1f / 2f, -x + 1f / 2f, y }, { -z + 1f / 2f, x, -y + 1f / 2f }, { y, z, x },
				{ -y + 1f / 2f, z, -x + 1f / 2f }, { y, -z + 1f / 2f, -x + 1f / 2f }, { -y + 1f / 2f, -z + 1f / 2f, x },
				{ y, x, -z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { y, -x + 1f / 2f, z },
				{ -y + 1f / 2f, x, z }, { x, z, -y + 1f / 2f }, { -x + 1f / 2f, z, y },
				{ -x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f }, { x, -z + 1f / 2f, y }, { z, y, -x + 1f / 2f },
				{ z, -y + 1f / 2f, x }, { -z + 1f / 2f, y, x }, { -z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f },
				{ -x, -y, -z }, { x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y, z + 1f / 2f },
				{ -x, y + 1f / 2f, z + 1f / 2f }, { -z, -x, -y }, { -z, x + 1f / 2f, y + 1f / 2f },
				{ z + 1f / 2f, x + 1f / 2f, -y }, { z + 1f / 2f, -x, y + 1f / 2f }, { -y, -z, -x },
				{ y + 1f / 2f, -z, x + 1f / 2f }, { -y, z + 1f / 2f, x + 1f / 2f }, { y + 1f / 2f, z + 1f / 2f, -x },
				{ -y, -x, z + 1f / 2f }, { y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, { -y, x + 1f / 2f, -z },
				{ y + 1f / 2f, -x, -z }, { -x, -z, y + 1f / 2f }, { x + 1f / 2f, -z, -y },
				{ x + 1f / 2f, z + 1f / 2f, y + 1f / 2f }, { -x, z + 1f / 2f, -y }, { -z, -y, x + 1f / 2f },
				{ -z, y + 1f / 2f, -x }, { z + 1f / 2f, -y, -x }, { z + 1f / 2f, y + 1f / 2f, x + 1f / 2f }, };
	}

	public static float[][] pos223(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x },
				{ y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, z + 1f / 2f, -y + 1f / 2f }, { -x + 1f / 2f, z + 1f / 2f, y + 1f / 2f },
				{ -x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f }, { x + 1f / 2f, -z + 1f / 2f, y + 1f / 2f },
				{ z + 1f / 2f, y + 1f / 2f, -x + 1f / 2f }, { z + 1f / 2f, -y + 1f / 2f, x + 1f / 2f },
				{ -z + 1f / 2f, y + 1f / 2f, x + 1f / 2f }, { -z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f },
				{ -x, -y, -z }, { x, y, -z }, { x, -y, z }, { -x, y, z }, { -z, -x, -y }, { -z, x, y }, { z, x, -y },
				{ z, -x, y }, { -y, -z, -x }, { y, -z, x }, { -y, z, x }, { y, z, -x },
				{ -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ -x + 1f / 2f, -z + 1f / 2f, y + 1f / 2f }, { x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f },
				{ x + 1f / 2f, z + 1f / 2f, y + 1f / 2f }, { -x + 1f / 2f, z + 1f / 2f, -y + 1f / 2f },
				{ -z + 1f / 2f, -y + 1f / 2f, x + 1f / 2f }, { -z + 1f / 2f, y + 1f / 2f, -x + 1f / 2f },
				{ z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f }, { z + 1f / 2f, y + 1f / 2f, x + 1f / 2f }, };
	}

	public static float[][] pos224(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x },
				{ y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, z + 1f / 2f, -y + 1f / 2f }, { -x + 1f / 2f, z + 1f / 2f, y + 1f / 2f },
				{ -x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f }, { x + 1f / 2f, -z + 1f / 2f, y + 1f / 2f },
				{ z + 1f / 2f, y + 1f / 2f, -x + 1f / 2f }, { z + 1f / 2f, -y + 1f / 2f, x + 1f / 2f },
				{ -z + 1f / 2f, y + 1f / 2f, x + 1f / 2f }, { -z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -z + 1f / 2f, -x + 1f / 2f, -y + 1f / 2f }, { -z + 1f / 2f, x + 1f / 2f, y + 1f / 2f },
				{ z + 1f / 2f, x + 1f / 2f, -y + 1f / 2f }, { z + 1f / 2f, -x + 1f / 2f, y + 1f / 2f },
				{ -y + 1f / 2f, -z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, x + 1f / 2f },
				{ -y + 1f / 2f, z + 1f / 2f, x + 1f / 2f }, { y + 1f / 2f, z + 1f / 2f, -x + 1f / 2f }, { -y, -x, z },
				{ y, x, z }, { -y, x, -z }, { y, -x, -z }, { -x, -z, y }, { x, -z, -y }, { x, z, y }, { -x, z, -y },
				{ -z, -y, x }, { -z, y, -x }, { z, -y, -x }, { z, y, x }, };
	}

	public static float[][] pos224alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y, -z + 1f / 2f },
				{ x, -y + 1f / 2f, -z + 1f / 2f }, { z, x, y }, { z, -x + 1f / 2f, -y + 1f / 2f },
				{ -z + 1f / 2f, -x + 1f / 2f, y }, { -z + 1f / 2f, x, -y + 1f / 2f }, { y, z, x },
				{ -y + 1f / 2f, z, -x + 1f / 2f }, { y, -z + 1f / 2f, -x + 1f / 2f }, { -y + 1f / 2f, -z + 1f / 2f, x },
				{ y + 1f / 2f, x + 1f / 2f, -z }, { -y, -x, -z }, { y + 1f / 2f, -x, z + 1f / 2f },
				{ -y, x + 1f / 2f, z + 1f / 2f }, { x + 1f / 2f, z + 1f / 2f, -y }, { -x, z + 1f / 2f, y + 1f / 2f },
				{ -x, -z, -y }, { x + 1f / 2f, -z, y + 1f / 2f }, { z + 1f / 2f, y + 1f / 2f, -x },
				{ z + 1f / 2f, -y, x + 1f / 2f }, { -z, y + 1f / 2f, x + 1f / 2f }, { -z, -y, -x }, { -x, -y, -z },
				{ x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, z + 1f / 2f },
				{ -z, -x, -y }, { -z, x + 1f / 2f, y + 1f / 2f }, { z + 1f / 2f, x + 1f / 2f, -y },
				{ z + 1f / 2f, -x, y + 1f / 2f }, { -y, -z, -x }, { y + 1f / 2f, -z, x + 1f / 2f },
				{ -y, z + 1f / 2f, x + 1f / 2f }, { y + 1f / 2f, z + 1f / 2f, -x }, { -y + 1f / 2f, -x + 1f / 2f, z },
				{ y, x, z }, { -y + 1f / 2f, x, -z + 1f / 2f }, { y, -x + 1f / 2f, -z + 1f / 2f },
				{ -x + 1f / 2f, -z + 1f / 2f, y }, { x, -z + 1f / 2f, -y + 1f / 2f }, { x, z, y },
				{ -x + 1f / 2f, z, -y + 1f / 2f }, { -z + 1f / 2f, -y + 1f / 2f, x }, { -z + 1f / 2f, y, -x + 1f / 2f },
				{ z, -y + 1f / 2f, -x + 1f / 2f }, { z, y, x }, };
	}

	public static float[][] pos225(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x }, { y, x, -z },
				{ -y, -x, -z }, { y, -x, z }, { -y, x, z }, { x, z, -y }, { -x, z, y }, { -x, -z, -y }, { x, -z, y },
				{ z, y, -x }, { z, -y, x }, { -z, y, x }, { -z, -y, -x }, { -x, -y, -z }, { x, y, -z }, { x, -y, z },
				{ -x, y, z }, { -z, -x, -y }, { -z, x, y }, { z, x, -y }, { z, -x, y }, { -y, -z, -x }, { y, -z, x },
				{ -y, z, x }, { y, z, -x }, { -y, -x, z }, { y, x, z }, { -y, x, -z }, { y, -x, -z }, { -x, -z, y },
				{ x, -z, -y }, { x, z, y }, { -x, z, -y }, { -z, -y, x }, { -z, y, -x }, { z, -y, -x }, { z, y, x },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x, -y + 1f / 2f, -z + 1f / 2f }, { z, x + 1f / 2f, y + 1f / 2f }, { z, -x + 1f / 2f, -y + 1f / 2f },
				{ -z, -x + 1f / 2f, y + 1f / 2f }, { -z, x + 1f / 2f, -y + 1f / 2f }, { y, z + 1f / 2f, x + 1f / 2f },
				{ -y, z + 1f / 2f, -x + 1f / 2f }, { y, -z + 1f / 2f, -x + 1f / 2f }, { -y, -z + 1f / 2f, x + 1f / 2f },
				{ y, x + 1f / 2f, -z + 1f / 2f }, { -y, -x + 1f / 2f, -z + 1f / 2f }, { y, -x + 1f / 2f, z + 1f / 2f },
				{ -y, x + 1f / 2f, z + 1f / 2f }, { x, z + 1f / 2f, -y + 1f / 2f }, { -x, z + 1f / 2f, y + 1f / 2f },
				{ -x, -z + 1f / 2f, -y + 1f / 2f }, { x, -z + 1f / 2f, y + 1f / 2f }, { z, y + 1f / 2f, -x + 1f / 2f },
				{ z, -y + 1f / 2f, x + 1f / 2f }, { -z, y + 1f / 2f, x + 1f / 2f }, { -z, -y + 1f / 2f, -x + 1f / 2f },
				{ -x, -y + 1f / 2f, -z + 1f / 2f }, { x, y + 1f / 2f, -z + 1f / 2f }, { x, -y + 1f / 2f, z + 1f / 2f },
				{ -x, y + 1f / 2f, z + 1f / 2f }, { -z, -x + 1f / 2f, -y + 1f / 2f }, { -z, x + 1f / 2f, y + 1f / 2f },
				{ z, x + 1f / 2f, -y + 1f / 2f }, { z, -x + 1f / 2f, y + 1f / 2f }, { -y, -z + 1f / 2f, -x + 1f / 2f },
				{ y, -z + 1f / 2f, x + 1f / 2f }, { -y, z + 1f / 2f, x + 1f / 2f }, { y, z + 1f / 2f, -x + 1f / 2f },
				{ -y, -x + 1f / 2f, z + 1f / 2f }, { y, x + 1f / 2f, z + 1f / 2f }, { -y, x + 1f / 2f, -z + 1f / 2f },
				{ y, -x + 1f / 2f, -z + 1f / 2f }, { -x, -z + 1f / 2f, y + 1f / 2f }, { x, -z + 1f / 2f, -y + 1f / 2f },
				{ x, z + 1f / 2f, y + 1f / 2f }, { -x, z + 1f / 2f, -y + 1f / 2f }, { -z, -y + 1f / 2f, x + 1f / 2f },
				{ -z, y + 1f / 2f, -x + 1f / 2f }, { z, -y + 1f / 2f, -x + 1f / 2f }, { z, y + 1f / 2f, x + 1f / 2f },
				{ x + 1f / 2f, y, z + 1f / 2f }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x + 1f / 2f, y, -z + 1f / 2f },
				{ x + 1f / 2f, -y, -z + 1f / 2f }, { z + 1f / 2f, x, y + 1f / 2f }, { z + 1f / 2f, -x, -y + 1f / 2f },
				{ -z + 1f / 2f, -x, y + 1f / 2f }, { -z + 1f / 2f, x, -y + 1f / 2f }, { y + 1f / 2f, z, x + 1f / 2f },
				{ -y + 1f / 2f, z, -x + 1f / 2f }, { y + 1f / 2f, -z, -x + 1f / 2f }, { -y + 1f / 2f, -z, x + 1f / 2f },
				{ y + 1f / 2f, x, -z + 1f / 2f }, { -y + 1f / 2f, -x, -z + 1f / 2f }, { y + 1f / 2f, -x, z + 1f / 2f },
				{ -y + 1f / 2f, x, z + 1f / 2f }, { x + 1f / 2f, z, -y + 1f / 2f }, { -x + 1f / 2f, z, y + 1f / 2f },
				{ -x + 1f / 2f, -z, -y + 1f / 2f }, { x + 1f / 2f, -z, y + 1f / 2f }, { z + 1f / 2f, y, -x + 1f / 2f },
				{ z + 1f / 2f, -y, x + 1f / 2f }, { -z + 1f / 2f, y, x + 1f / 2f }, { -z + 1f / 2f, -y, -x + 1f / 2f },
				{ -x + 1f / 2f, -y, -z + 1f / 2f }, { x + 1f / 2f, y, -z + 1f / 2f }, { x + 1f / 2f, -y, z + 1f / 2f },
				{ -x + 1f / 2f, y, z + 1f / 2f }, { -z + 1f / 2f, -x, -y + 1f / 2f }, { -z + 1f / 2f, x, y + 1f / 2f },
				{ z + 1f / 2f, x, -y + 1f / 2f }, { z + 1f / 2f, -x, y + 1f / 2f }, { -y + 1f / 2f, -z, -x + 1f / 2f },
				{ y + 1f / 2f, -z, x + 1f / 2f }, { -y + 1f / 2f, z, x + 1f / 2f }, { y + 1f / 2f, z, -x + 1f / 2f },
				{ -y + 1f / 2f, -x, z + 1f / 2f }, { y + 1f / 2f, x, z + 1f / 2f }, { -y + 1f / 2f, x, -z + 1f / 2f },
				{ y + 1f / 2f, -x, -z + 1f / 2f }, { -x + 1f / 2f, -z, y + 1f / 2f }, { x + 1f / 2f, -z, -y + 1f / 2f },
				{ x + 1f / 2f, z, y + 1f / 2f }, { -x + 1f / 2f, z, -y + 1f / 2f }, { -z + 1f / 2f, -y, x + 1f / 2f },
				{ -z + 1f / 2f, y, -x + 1f / 2f }, { z + 1f / 2f, -y, -x + 1f / 2f }, { z + 1f / 2f, y, x + 1f / 2f },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z + 1f / 2f, x + 1f / 2f, y }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z + 1f / 2f, -x + 1f / 2f, y }, { -z + 1f / 2f, x + 1f / 2f, -y }, { y + 1f / 2f, z + 1f / 2f, x },
				{ -y + 1f / 2f, z + 1f / 2f, -x }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y + 1f / 2f, -z + 1f / 2f, x },
				{ y + 1f / 2f, x + 1f / 2f, -z }, { -y + 1f / 2f, -x + 1f / 2f, -z }, { y + 1f / 2f, -x + 1f / 2f, z },
				{ -y + 1f / 2f, x + 1f / 2f, z }, { x + 1f / 2f, z + 1f / 2f, -y }, { -x + 1f / 2f, z + 1f / 2f, y },
				{ -x + 1f / 2f, -z + 1f / 2f, -y }, { x + 1f / 2f, -z + 1f / 2f, y }, { z + 1f / 2f, y + 1f / 2f, -x },
				{ z + 1f / 2f, -y + 1f / 2f, x }, { -z + 1f / 2f, y + 1f / 2f, x }, { -z + 1f / 2f, -y + 1f / 2f, -x },
				{ -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z }, { -z + 1f / 2f, -x + 1f / 2f, -y }, { -z + 1f / 2f, x + 1f / 2f, y },
				{ z + 1f / 2f, x + 1f / 2f, -y }, { z + 1f / 2f, -x + 1f / 2f, y }, { -y + 1f / 2f, -z + 1f / 2f, -x },
				{ y + 1f / 2f, -z + 1f / 2f, x }, { -y + 1f / 2f, z + 1f / 2f, x }, { y + 1f / 2f, z + 1f / 2f, -x },
				{ -y + 1f / 2f, -x + 1f / 2f, z }, { y + 1f / 2f, x + 1f / 2f, z }, { -y + 1f / 2f, x + 1f / 2f, -z },
				{ y + 1f / 2f, -x + 1f / 2f, -z }, { -x + 1f / 2f, -z + 1f / 2f, y }, { x + 1f / 2f, -z + 1f / 2f, -y },
				{ x + 1f / 2f, z + 1f / 2f, y }, { -x + 1f / 2f, z + 1f / 2f, -y }, { -z + 1f / 2f, -y + 1f / 2f, x },
				{ -z + 1f / 2f, y + 1f / 2f, -x }, { z + 1f / 2f, -y + 1f / 2f, -x },
				{ z + 1f / 2f, y + 1f / 2f, x }, };
	}

	public static float[][] pos226(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x },
				{ y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, z + 1f / 2f, -y + 1f / 2f }, { -x + 1f / 2f, z + 1f / 2f, y + 1f / 2f },
				{ -x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f }, { x + 1f / 2f, -z + 1f / 2f, y + 1f / 2f },
				{ z + 1f / 2f, y + 1f / 2f, -x + 1f / 2f }, { z + 1f / 2f, -y + 1f / 2f, x + 1f / 2f },
				{ -z + 1f / 2f, y + 1f / 2f, x + 1f / 2f }, { -z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f },
				{ -x, -y, -z }, { x, y, -z }, { x, -y, z }, { -x, y, z }, { -z, -x, -y }, { -z, x, y }, { z, x, -y },
				{ z, -x, y }, { -y, -z, -x }, { y, -z, x }, { -y, z, x }, { y, z, -x },
				{ -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ -x + 1f / 2f, -z + 1f / 2f, y + 1f / 2f }, { x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f },
				{ x + 1f / 2f, z + 1f / 2f, y + 1f / 2f }, { -x + 1f / 2f, z + 1f / 2f, -y + 1f / 2f },
				{ -z + 1f / 2f, -y + 1f / 2f, x + 1f / 2f }, { -z + 1f / 2f, y + 1f / 2f, -x + 1f / 2f },
				{ z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f }, { z + 1f / 2f, y + 1f / 2f, x + 1f / 2f },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x, -y + 1f / 2f, -z + 1f / 2f }, { z, x + 1f / 2f, y + 1f / 2f }, { z, -x + 1f / 2f, -y + 1f / 2f },
				{ -z, -x + 1f / 2f, y + 1f / 2f }, { -z, x + 1f / 2f, -y + 1f / 2f }, { y, z + 1f / 2f, x + 1f / 2f },
				{ -y, z + 1f / 2f, -x + 1f / 2f }, { y, -z + 1f / 2f, -x + 1f / 2f }, { -y, -z + 1f / 2f, x + 1f / 2f },
				{ y + 1f / 2f, x, -z }, { -y + 1f / 2f, -x, -z }, { y + 1f / 2f, -x, z }, { -y + 1f / 2f, x, z },
				{ x + 1f / 2f, z, -y }, { -x + 1f / 2f, z, y }, { -x + 1f / 2f, -z, -y }, { x + 1f / 2f, -z, y },
				{ z + 1f / 2f, y, -x }, { z + 1f / 2f, -y, x }, { -z + 1f / 2f, y, x }, { -z + 1f / 2f, -y, -x },
				{ -x, -y + 1f / 2f, -z + 1f / 2f }, { x, y + 1f / 2f, -z + 1f / 2f }, { x, -y + 1f / 2f, z + 1f / 2f },
				{ -x, y + 1f / 2f, z + 1f / 2f }, { -z, -x + 1f / 2f, -y + 1f / 2f }, { -z, x + 1f / 2f, y + 1f / 2f },
				{ z, x + 1f / 2f, -y + 1f / 2f }, { z, -x + 1f / 2f, y + 1f / 2f }, { -y, -z + 1f / 2f, -x + 1f / 2f },
				{ y, -z + 1f / 2f, x + 1f / 2f }, { -y, z + 1f / 2f, x + 1f / 2f }, { y, z + 1f / 2f, -x + 1f / 2f },
				{ -y + 1f / 2f, -x, z }, { y + 1f / 2f, x, z }, { -y + 1f / 2f, x, -z }, { y + 1f / 2f, -x, -z },
				{ -x + 1f / 2f, -z, y }, { x + 1f / 2f, -z, -y }, { x + 1f / 2f, z, y }, { -x + 1f / 2f, z, -y },
				{ -z + 1f / 2f, -y, x }, { -z + 1f / 2f, y, -x }, { z + 1f / 2f, -y, -x }, { z + 1f / 2f, y, x },
				{ x + 1f / 2f, y, z + 1f / 2f }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x + 1f / 2f, y, -z + 1f / 2f },
				{ x + 1f / 2f, -y, -z + 1f / 2f }, { z + 1f / 2f, x, y + 1f / 2f }, { z + 1f / 2f, -x, -y + 1f / 2f },
				{ -z + 1f / 2f, -x, y + 1f / 2f }, { -z + 1f / 2f, x, -y + 1f / 2f }, { y + 1f / 2f, z, x + 1f / 2f },
				{ -y + 1f / 2f, z, -x + 1f / 2f }, { y + 1f / 2f, -z, -x + 1f / 2f }, { -y + 1f / 2f, -z, x + 1f / 2f },
				{ y, x + 1f / 2f, -z }, { -y, -x + 1f / 2f, -z }, { y, -x + 1f / 2f, z }, { -y, x + 1f / 2f, z },
				{ x, z + 1f / 2f, -y }, { -x, z + 1f / 2f, y }, { -x, -z + 1f / 2f, -y }, { x, -z + 1f / 2f, y },
				{ z, y + 1f / 2f, -x }, { z, -y + 1f / 2f, x }, { -z, y + 1f / 2f, x }, { -z, -y + 1f / 2f, -x },
				{ -x + 1f / 2f, -y, -z + 1f / 2f }, { x + 1f / 2f, y, -z + 1f / 2f }, { x + 1f / 2f, -y, z + 1f / 2f },
				{ -x + 1f / 2f, y, z + 1f / 2f }, { -z + 1f / 2f, -x, -y + 1f / 2f }, { -z + 1f / 2f, x, y + 1f / 2f },
				{ z + 1f / 2f, x, -y + 1f / 2f }, { z + 1f / 2f, -x, y + 1f / 2f }, { -y + 1f / 2f, -z, -x + 1f / 2f },
				{ y + 1f / 2f, -z, x + 1f / 2f }, { -y + 1f / 2f, z, x + 1f / 2f }, { y + 1f / 2f, z, -x + 1f / 2f },
				{ -y, -x + 1f / 2f, z }, { y, x + 1f / 2f, z }, { -y, x + 1f / 2f, -z }, { y, -x + 1f / 2f, -z },
				{ -x, -z + 1f / 2f, y }, { x, -z + 1f / 2f, -y }, { x, z + 1f / 2f, y }, { -x, z + 1f / 2f, -y },
				{ -z, -y + 1f / 2f, x }, { -z, y + 1f / 2f, -x }, { z, -y + 1f / 2f, -x }, { z, y + 1f / 2f, x },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z + 1f / 2f, x + 1f / 2f, y }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z + 1f / 2f, -x + 1f / 2f, y }, { -z + 1f / 2f, x + 1f / 2f, -y }, { y + 1f / 2f, z + 1f / 2f, x },
				{ -y + 1f / 2f, z + 1f / 2f, -x }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y + 1f / 2f, -z + 1f / 2f, x },
				{ y, x, -z + 1f / 2f }, { -y, -x, -z + 1f / 2f }, { y, -x, z + 1f / 2f }, { -y, x, z + 1f / 2f },
				{ x, z, -y + 1f / 2f }, { -x, z, y + 1f / 2f }, { -x, -z, -y + 1f / 2f }, { x, -z, y + 1f / 2f },
				{ z, y, -x + 1f / 2f }, { z, -y, x + 1f / 2f }, { -z, y, x + 1f / 2f }, { -z, -y, -x + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 1f / 2f, y + 1f / 2f, -z }, { x + 1f / 2f, -y + 1f / 2f, z },
				{ -x + 1f / 2f, y + 1f / 2f, z }, { -z + 1f / 2f, -x + 1f / 2f, -y }, { -z + 1f / 2f, x + 1f / 2f, y },
				{ z + 1f / 2f, x + 1f / 2f, -y }, { z + 1f / 2f, -x + 1f / 2f, y }, { -y + 1f / 2f, -z + 1f / 2f, -x },
				{ y + 1f / 2f, -z + 1f / 2f, x }, { -y + 1f / 2f, z + 1f / 2f, x }, { y + 1f / 2f, z + 1f / 2f, -x },
				{ -y, -x, z + 1f / 2f }, { y, x, z + 1f / 2f }, { -y, x, -z + 1f / 2f }, { y, -x, -z + 1f / 2f },
				{ -x, -z, y + 1f / 2f }, { x, -z, -y + 1f / 2f }, { x, z, y + 1f / 2f }, { -x, z, -y + 1f / 2f },
				{ -z, -y, x + 1f / 2f }, { -z, y, -x + 1f / 2f }, { z, -y, -x + 1f / 2f }, { z, y, x + 1f / 2f }, };
	}

	public static float[][] pos227(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y, -z + 1f / 2f }, { z, x, y }, { z + 1f / 2f, -x, -y + 1f / 2f },
				{ -z, -x + 1f / 2f, y + 1f / 2f }, { -z + 1f / 2f, x + 1f / 2f, -y }, { y, z, x },
				{ -y + 1f / 2f, z + 1f / 2f, -x }, { y + 1f / 2f, -z, -x + 1f / 2f }, { -y, -z + 1f / 2f, x + 1f / 2f },
				{ y + 3f / 4f, x + 1f / 4f, -z + 3f / 4f }, { -y + 1f / 4f, -x + 1f / 4f, -z + 1f / 4f },
				{ y + 1f / 4f, -x + 3f / 4f, z + 3f / 4f }, { -y + 3f / 4f, x + 3f / 4f, z + 1f / 4f },
				{ x + 3f / 4f, z + 1f / 4f, -y + 3f / 4f }, { -x + 3f / 4f, z + 3f / 4f, y + 1f / 4f },
				{ -x + 1f / 4f, -z + 1f / 4f, -y + 1f / 4f }, { x + 1f / 4f, -z + 3f / 4f, y + 3f / 4f },
				{ z + 3f / 4f, y + 1f / 4f, -x + 3f / 4f }, { z + 1f / 4f, -y + 3f / 4f, x + 3f / 4f },
				{ -z + 3f / 4f, y + 3f / 4f, x + 1f / 4f }, { -z + 1f / 4f, -y + 1f / 4f, -x + 1f / 4f },
				{ -x + 1f / 4f, -y + 1f / 4f, -z + 1f / 4f }, { x + 1f / 4f, y + 3f / 4f, -z + 3f / 4f },
				{ x + 3f / 4f, -y + 3f / 4f, z + 1f / 4f }, { -x + 3f / 4f, y + 1f / 4f, z + 3f / 4f },
				{ -z + 1f / 4f, -x + 1f / 4f, -y + 1f / 4f }, { -z + 3f / 4f, x + 1f / 4f, y + 3f / 4f },
				{ z + 1f / 4f, x + 3f / 4f, -y + 3f / 4f }, { z + 3f / 4f, -x + 3f / 4f, y + 1f / 4f },
				{ -y + 1f / 4f, -z + 1f / 4f, -x + 1f / 4f }, { y + 3f / 4f, -z + 3f / 4f, x + 1f / 4f },
				{ -y + 3f / 4f, z + 1f / 4f, x + 3f / 4f }, { y + 1f / 4f, z + 3f / 4f, -x + 3f / 4f },
				{ -y + 1f / 2f, -x, z + 1f / 2f }, { y, x, z }, { -y, x + 1f / 2f, -z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, -z }, { -x + 1f / 2f, -z, y + 1f / 2f }, { x + 1f / 2f, -z + 1f / 2f, -y },
				{ x, z, y }, { -x, z + 1f / 2f, -y + 1f / 2f }, { -z + 1f / 2f, -y, x + 1f / 2f },
				{ -z, y + 1f / 2f, -x + 1f / 2f }, { z + 1f / 2f, -y + 1f / 2f, -x }, { z, y, x },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x, -y, z }, { -x + 1f / 2f, y, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z, x + 1f / 2f, y + 1f / 2f }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z, -x, y }, { -z + 1f / 2f, x, -y + 1f / 2f }, { y, z + 1f / 2f, x + 1f / 2f },
				{ -y + 1f / 2f, z, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y, -z, x },
				{ y + 3f / 4f, x + 3f / 4f, -z + 1f / 4f }, { -y + 1f / 4f, -x + 3f / 4f, -z + 3f / 4f },
				{ y + 1f / 4f, -x + 1f / 4f, z + 1f / 4f }, { -y + 3f / 4f, x + 1f / 4f, z + 3f / 4f },
				{ x + 3f / 4f, z + 3f / 4f, -y + 1f / 4f }, { -x + 3f / 4f, z + 1f / 4f, y + 3f / 4f },
				{ -x + 1f / 4f, -z + 3f / 4f, -y + 3f / 4f }, { x + 1f / 4f, -z + 1f / 4f, y + 1f / 4f },
				{ z + 3f / 4f, y + 3f / 4f, -x + 1f / 4f }, { z + 1f / 4f, -y + 1f / 4f, x + 1f / 4f },
				{ -z + 3f / 4f, y + 1f / 4f, x + 3f / 4f }, { -z + 1f / 4f, -y + 3f / 4f, -x + 3f / 4f },
				{ -x + 1f / 4f, -y + 3f / 4f, -z + 3f / 4f }, { x + 1f / 4f, y + 1f / 4f, -z + 1f / 4f },
				{ x + 3f / 4f, -y + 1f / 4f, z + 3f / 4f }, { -x + 3f / 4f, y + 3f / 4f, z + 1f / 4f },
				{ -z + 1f / 4f, -x + 3f / 4f, -y + 3f / 4f }, { -z + 3f / 4f, x + 3f / 4f, y + 1f / 4f },
				{ z + 1f / 4f, x + 1f / 4f, -y + 1f / 4f }, { z + 3f / 4f, -x + 1f / 4f, y + 3f / 4f },
				{ -y + 1f / 4f, -z + 3f / 4f, -x + 3f / 4f }, { y + 3f / 4f, -z + 1f / 4f, x + 3f / 4f },
				{ -y + 3f / 4f, z + 3f / 4f, x + 1f / 4f }, { y + 1f / 4f, z + 1f / 4f, -x + 1f / 4f },
				{ -y + 1f / 2f, -x + 1f / 2f, z }, { y, x + 1f / 2f, z + 1f / 2f }, { -y, x, -z },
				{ y + 1f / 2f, -x, -z + 1f / 2f }, { -x + 1f / 2f, -z + 1f / 2f, y }, { x + 1f / 2f, -z, -y + 1f / 2f },
				{ x, z + 1f / 2f, y + 1f / 2f }, { -x, z, -y }, { -z + 1f / 2f, -y + 1f / 2f, x }, { -z, y, -x },
				{ z + 1f / 2f, -y, -x + 1f / 2f }, { z, y + 1f / 2f, x + 1f / 2f }, { x + 1f / 2f, y, z + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, z }, { -x, y + 1f / 2f, -z + 1f / 2f }, { x, -y, -z },
				{ z + 1f / 2f, x, y + 1f / 2f }, { z, -x, -y }, { -z + 1f / 2f, -x + 1f / 2f, y },
				{ -z, x + 1f / 2f, -y + 1f / 2f }, { y + 1f / 2f, z, x + 1f / 2f }, { -y, z + 1f / 2f, -x + 1f / 2f },
				{ y, -z, -x }, { -y + 1f / 2f, -z + 1f / 2f, x }, { y + 1f / 4f, x + 1f / 4f, -z + 1f / 4f },
				{ -y + 3f / 4f, -x + 1f / 4f, -z + 3f / 4f }, { y + 3f / 4f, -x + 3f / 4f, z + 1f / 4f },
				{ -y + 1f / 4f, x + 3f / 4f, z + 3f / 4f }, { x + 1f / 4f, z + 1f / 4f, -y + 1f / 4f },
				{ -x + 1f / 4f, z + 3f / 4f, y + 3f / 4f }, { -x + 3f / 4f, -z + 1f / 4f, -y + 3f / 4f },
				{ x + 3f / 4f, -z + 3f / 4f, y + 1f / 4f }, { z + 1f / 4f, y + 1f / 4f, -x + 1f / 4f },
				{ z + 3f / 4f, -y + 3f / 4f, x + 1f / 4f }, { -z + 1f / 4f, y + 3f / 4f, x + 3f / 4f },
				{ -z + 3f / 4f, -y + 1f / 4f, -x + 3f / 4f }, { -x + 3f / 4f, -y + 1f / 4f, -z + 3f / 4f },
				{ x + 3f / 4f, y + 3f / 4f, -z + 1f / 4f }, { x + 1f / 4f, -y + 3f / 4f, z + 3f / 4f },
				{ -x + 1f / 4f, y + 1f / 4f, z + 1f / 4f }, { -z + 3f / 4f, -x + 1f / 4f, -y + 3f / 4f },
				{ -z + 1f / 4f, x + 1f / 4f, y + 1f / 4f }, { z + 3f / 4f, x + 3f / 4f, -y + 1f / 4f },
				{ z + 1f / 4f, -x + 3f / 4f, y + 3f / 4f }, { -y + 3f / 4f, -z + 1f / 4f, -x + 3f / 4f },
				{ y + 1f / 4f, -z + 3f / 4f, x + 3f / 4f }, { -y + 1f / 4f, z + 1f / 4f, x + 1f / 4f },
				{ y + 3f / 4f, z + 3f / 4f, -x + 1f / 4f }, { -y, -x, z }, { y + 1f / 2f, x, z + 1f / 2f },
				{ -y + 1f / 2f, x + 1f / 2f, -z }, { y, -x + 1f / 2f, -z + 1f / 2f }, { -x, -z, y },
				{ x, -z + 1f / 2f, -y + 1f / 2f }, { x + 1f / 2f, z, y + 1f / 2f }, { -x + 1f / 2f, z + 1f / 2f, -y },
				{ -z, -y, x }, { -z + 1f / 2f, y + 1f / 2f, -x }, { z, -y + 1f / 2f, -x + 1f / 2f },
				{ z + 1f / 2f, y, x + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, z }, { -x + 1f / 2f, -y, z + 1f / 2f },
				{ -x, y, -z }, { x, -y + 1f / 2f, -z + 1f / 2f }, { z + 1f / 2f, x + 1f / 2f, y },
				{ z, -x + 1f / 2f, -y + 1f / 2f }, { -z + 1f / 2f, -x, y + 1f / 2f }, { -z, x, -y },
				{ y + 1f / 2f, z + 1f / 2f, x }, { -y, z, -x }, { y, -z + 1f / 2f, -x + 1f / 2f },
				{ -y + 1f / 2f, -z, x + 1f / 2f }, { y + 1f / 4f, x + 3f / 4f, -z + 3f / 4f },
				{ -y + 3f / 4f, -x + 3f / 4f, -z + 1f / 4f }, { y + 3f / 4f, -x + 1f / 4f, z + 3f / 4f },
				{ -y + 1f / 4f, x + 1f / 4f, z + 1f / 4f }, { x + 1f / 4f, z + 3f / 4f, -y + 3f / 4f },
				{ -x + 1f / 4f, z + 1f / 4f, y + 1f / 4f }, { -x + 3f / 4f, -z + 3f / 4f, -y + 1f / 4f },
				{ x + 3f / 4f, -z + 1f / 4f, y + 3f / 4f }, { z + 1f / 4f, y + 3f / 4f, -x + 3f / 4f },
				{ z + 3f / 4f, -y + 1f / 4f, x + 3f / 4f }, { -z + 1f / 4f, y + 1f / 4f, x + 1f / 4f },
				{ -z + 3f / 4f, -y + 3f / 4f, -x + 1f / 4f }, { -x + 3f / 4f, -y + 3f / 4f, -z + 1f / 4f },
				{ x + 3f / 4f, y + 1f / 4f, -z + 3f / 4f }, { x + 1f / 4f, -y + 1f / 4f, z + 1f / 4f },
				{ -x + 1f / 4f, y + 3f / 4f, z + 3f / 4f }, { -z + 3f / 4f, -x + 3f / 4f, -y + 1f / 4f },
				{ -z + 1f / 4f, x + 3f / 4f, y + 3f / 4f }, { z + 3f / 4f, x + 1f / 4f, -y + 3f / 4f },
				{ z + 1f / 4f, -x + 1f / 4f, y + 1f / 4f }, { -y + 3f / 4f, -z + 3f / 4f, -x + 1f / 4f },
				{ y + 1f / 4f, -z + 1f / 4f, x + 1f / 4f }, { -y + 1f / 4f, z + 3f / 4f, x + 3f / 4f },
				{ y + 3f / 4f, z + 1f / 4f, -x + 3f / 4f }, { -y, -x + 1f / 2f, z + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, z }, { -y + 1f / 2f, x, -z + 1f / 2f }, { y, -x, -z },
				{ -x, -z + 1f / 2f, y + 1f / 2f }, { x, -z, -y }, { x + 1f / 2f, z + 1f / 2f, y },
				{ -x + 1f / 2f, z, -y + 1f / 2f }, { -z, -y + 1f / 2f, x + 1f / 2f }, { -z + 1f / 2f, y, -x + 1f / 2f },
				{ z, -y, -x }, { z + 1f / 2f, y + 1f / 2f, x }, };
	}

	public static float[][] pos227alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 3f / 4f, -y + 1f / 4f, z + 1f / 2f },
				{ -x + 1f / 4f, y + 1f / 2f, -z + 3f / 4f }, { x + 1f / 2f, -y + 3f / 4f, -z + 1f / 4f }, { z, x, y },
				{ z + 1f / 2f, -x + 3f / 4f, -y + 1f / 4f }, { -z + 3f / 4f, -x + 1f / 4f, y + 1f / 2f },
				{ -z + 1f / 4f, x + 1f / 2f, -y + 3f / 4f }, { y, z, x }, { -y + 1f / 4f, z + 1f / 2f, -x + 3f / 4f },
				{ y + 1f / 2f, -z + 3f / 4f, -x + 1f / 4f }, { -y + 3f / 4f, -z + 1f / 4f, x + 1f / 2f },
				{ y + 3f / 4f, x + 1f / 4f, -z + 1f / 2f }, { -y, -x, -z }, { y + 1f / 4f, -x + 1f / 2f, z + 3f / 4f },
				{ -y + 1f / 2f, x + 3f / 4f, z + 1f / 4f }, { x + 3f / 4f, z + 1f / 4f, -y + 1f / 2f },
				{ -x + 1f / 2f, z + 3f / 4f, y + 1f / 4f }, { -x, -z, -y }, { x + 1f / 4f, -z + 1f / 2f, y + 3f / 4f },
				{ z + 3f / 4f, y + 1f / 4f, -x + 1f / 2f }, { z + 1f / 4f, -y + 1f / 2f, x + 3f / 4f },
				{ -z + 1f / 2f, y + 3f / 4f, x + 1f / 4f }, { -z, -y, -x }, { -x, -y, -z },
				{ x + 1f / 4f, y + 3f / 4f, -z + 1f / 2f }, { x + 3f / 4f, -y + 1f / 2f, z + 1f / 4f },
				{ -x + 1f / 2f, y + 1f / 4f, z + 3f / 4f }, { -z, -x, -y }, { -z + 1f / 2f, x + 1f / 4f, y + 3f / 4f },
				{ z + 1f / 4f, x + 3f / 4f, -y + 1f / 2f }, { z + 3f / 4f, -x + 1f / 2f, y + 1f / 4f }, { -y, -z, -x },
				{ y + 3f / 4f, -z + 1f / 2f, x + 1f / 4f }, { -y + 1f / 2f, z + 1f / 4f, x + 3f / 4f },
				{ y + 1f / 4f, z + 3f / 4f, -x + 1f / 2f }, { -y + 1f / 4f, -x + 3f / 4f, z + 1f / 2f }, { y, x, z },
				{ -y + 3f / 4f, x + 1f / 2f, -z + 1f / 4f }, { y + 1f / 2f, -x + 1f / 4f, -z + 3f / 4f },
				{ -x + 1f / 4f, -z + 3f / 4f, y + 1f / 2f }, { x + 1f / 2f, -z + 1f / 4f, -y + 3f / 4f }, { x, z, y },
				{ -x + 3f / 4f, z + 1f / 2f, -y + 1f / 4f }, { -z + 1f / 4f, -y + 3f / 4f, x + 1f / 2f },
				{ -z + 3f / 4f, y + 1f / 2f, -x + 1f / 4f }, { z + 1f / 2f, -y + 1f / 4f, -x + 3f / 4f }, { z, y, x },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x + 3f / 4f, -y + 3f / 4f, z }, { -x + 1f / 4f, y, -z + 1f / 4f },
				{ x + 1f / 2f, -y + 1f / 4f, -z + 3f / 4f }, { z, x + 1f / 2f, y + 1f / 2f },
				{ z + 1f / 2f, -x + 1f / 4f, -y + 3f / 4f }, { -z + 3f / 4f, -x + 3f / 4f, y },
				{ -z + 1f / 4f, x, -y + 1f / 4f }, { y, z + 1f / 2f, x + 1f / 2f }, { -y + 1f / 4f, z, -x + 1f / 4f },
				{ y + 1f / 2f, -z + 1f / 4f, -x + 3f / 4f }, { -y + 3f / 4f, -z + 3f / 4f, x },
				{ y + 3f / 4f, x + 3f / 4f, -z }, { -y, -x + 1f / 2f, -z + 1f / 2f }, { y + 1f / 4f, -x, z + 1f / 4f },
				{ -y + 1f / 2f, x + 1f / 4f, z + 3f / 4f }, { x + 3f / 4f, z + 3f / 4f, -y },
				{ -x + 1f / 2f, z + 1f / 4f, y + 3f / 4f }, { -x, -z + 1f / 2f, -y + 1f / 2f },
				{ x + 1f / 4f, -z, y + 1f / 4f }, { z + 3f / 4f, y + 3f / 4f, -x }, { z + 1f / 4f, -y, x + 1f / 4f },
				{ -z + 1f / 2f, y + 1f / 4f, x + 3f / 4f }, { -z, -y + 1f / 2f, -x + 1f / 2f },
				{ -x, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 4f, y + 1f / 4f, -z }, { x + 3f / 4f, -y, z + 3f / 4f },
				{ -x + 1f / 2f, y + 3f / 4f, z + 1f / 4f }, { -z, -x + 1f / 2f, -y + 1f / 2f },
				{ -z + 1f / 2f, x + 3f / 4f, y + 1f / 4f }, { z + 1f / 4f, x + 1f / 4f, -y },
				{ z + 3f / 4f, -x, y + 3f / 4f }, { -y, -z + 1f / 2f, -x + 1f / 2f }, { y + 3f / 4f, -z, x + 3f / 4f },
				{ -y + 1f / 2f, z + 3f / 4f, x + 1f / 4f }, { y + 1f / 4f, z + 1f / 4f, -x },
				{ -y + 1f / 4f, -x + 1f / 4f, z }, { y, x + 1f / 2f, z + 1f / 2f }, { -y + 3f / 4f, x, -z + 3f / 4f },
				{ y + 1f / 2f, -x + 3f / 4f, -z + 1f / 4f }, { -x + 1f / 4f, -z + 1f / 4f, y },
				{ x + 1f / 2f, -z + 3f / 4f, -y + 1f / 4f }, { x, z + 1f / 2f, y + 1f / 2f },
				{ -x + 3f / 4f, z, -y + 3f / 4f }, { -z + 1f / 4f, -y + 1f / 4f, x }, { -z + 3f / 4f, y, -x + 3f / 4f },
				{ z + 1f / 2f, -y + 3f / 4f, -x + 1f / 4f }, { z, y + 1f / 2f, x + 1f / 2f },
				{ x + 1f / 2f, y, z + 1f / 2f }, { -x + 1f / 4f, -y + 1f / 4f, z },
				{ -x + 3f / 4f, y + 1f / 2f, -z + 1f / 4f }, { x, -y + 3f / 4f, -z + 3f / 4f },
				{ z + 1f / 2f, x, y + 1f / 2f }, { z, -x + 3f / 4f, -y + 3f / 4f }, { -z + 1f / 4f, -x + 1f / 4f, y },
				{ -z + 3f / 4f, x + 1f / 2f, -y + 1f / 4f }, { y + 1f / 2f, z, x + 1f / 2f },
				{ -y + 3f / 4f, z + 1f / 2f, -x + 1f / 4f }, { y, -z + 3f / 4f, -x + 3f / 4f },
				{ -y + 1f / 4f, -z + 1f / 4f, x }, { y + 1f / 4f, x + 1f / 4f, -z }, { -y + 1f / 2f, -x, -z + 1f / 2f },
				{ y + 3f / 4f, -x + 1f / 2f, z + 1f / 4f }, { -y, x + 3f / 4f, z + 3f / 4f },
				{ x + 1f / 4f, z + 1f / 4f, -y }, { -x, z + 3f / 4f, y + 3f / 4f }, { -x + 1f / 2f, -z, -y + 1f / 2f },
				{ x + 3f / 4f, -z + 1f / 2f, y + 1f / 4f }, { z + 1f / 4f, y + 1f / 4f, -x },
				{ z + 3f / 4f, -y + 1f / 2f, x + 1f / 4f }, { -z, y + 3f / 4f, x + 3f / 4f },
				{ -z + 1f / 2f, -y, -x + 1f / 2f }, { -x + 1f / 2f, -y, -z + 1f / 2f },
				{ x + 3f / 4f, y + 3f / 4f, -z }, { x + 1f / 4f, -y + 1f / 2f, z + 3f / 4f },
				{ -x, y + 1f / 4f, z + 1f / 4f }, { -z + 1f / 2f, -x, -y + 1f / 2f }, { -z, x + 1f / 4f, y + 1f / 4f },
				{ z + 3f / 4f, x + 3f / 4f, -y }, { z + 1f / 4f, -x + 1f / 2f, y + 3f / 4f },
				{ -y + 1f / 2f, -z, -x + 1f / 2f }, { y + 1f / 4f, -z + 1f / 2f, x + 3f / 4f },
				{ -y, z + 1f / 4f, x + 1f / 4f }, { y + 3f / 4f, z + 3f / 4f, -x }, { -y + 3f / 4f, -x + 3f / 4f, z },
				{ y + 1f / 2f, x, z + 1f / 2f }, { -y + 1f / 4f, x + 1f / 2f, -z + 3f / 4f },
				{ y, -x + 1f / 4f, -z + 1f / 4f }, { -x + 3f / 4f, -z + 3f / 4f, y }, { x, -z + 1f / 4f, -y + 1f / 4f },
				{ x + 1f / 2f, z, y + 1f / 2f }, { -x + 1f / 4f, z + 1f / 2f, -y + 3f / 4f },
				{ -z + 3f / 4f, -y + 3f / 4f, x }, { -z + 1f / 4f, y + 1f / 2f, -x + 3f / 4f },
				{ z, -y + 1f / 4f, -x + 1f / 4f }, { z + 1f / 2f, y, x + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, z },
				{ -x + 1f / 4f, -y + 3f / 4f, z + 1f / 2f }, { -x + 3f / 4f, y, -z + 3f / 4f },
				{ x, -y + 1f / 4f, -z + 1f / 4f }, { z + 1f / 2f, x + 1f / 2f, y }, { z, -x + 1f / 4f, -y + 1f / 4f },
				{ -z + 1f / 4f, -x + 3f / 4f, y + 1f / 2f }, { -z + 3f / 4f, x, -y + 3f / 4f },
				{ y + 1f / 2f, z + 1f / 2f, x }, { -y + 3f / 4f, z, -x + 3f / 4f }, { y, -z + 1f / 4f, -x + 1f / 4f },
				{ -y + 1f / 4f, -z + 3f / 4f, x + 1f / 2f }, { y + 1f / 4f, x + 3f / 4f, -z + 1f / 2f },
				{ -y + 1f / 2f, -x + 1f / 2f, -z }, { y + 3f / 4f, -x, z + 3f / 4f }, { -y, x + 1f / 4f, z + 1f / 4f },
				{ x + 1f / 4f, z + 3f / 4f, -y + 1f / 2f }, { -x, z + 1f / 4f, y + 1f / 4f },
				{ -x + 1f / 2f, -z + 1f / 2f, -y }, { x + 3f / 4f, -z, y + 3f / 4f },
				{ z + 1f / 4f, y + 3f / 4f, -x + 1f / 2f }, { z + 3f / 4f, -y, x + 3f / 4f },
				{ -z, y + 1f / 4f, x + 1f / 4f }, { -z + 1f / 2f, -y + 1f / 2f, -x },
				{ -x + 1f / 2f, -y + 1f / 2f, -z }, { x + 3f / 4f, y + 1f / 4f, -z + 1f / 2f },
				{ x + 1f / 4f, -y, z + 1f / 4f }, { -x, y + 3f / 4f, z + 3f / 4f }, { -z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z, x + 3f / 4f, y + 3f / 4f }, { z + 3f / 4f, x + 1f / 4f, -y + 1f / 2f },
				{ z + 1f / 4f, -x, y + 1f / 4f }, { -y + 1f / 2f, -z + 1f / 2f, -x }, { y + 1f / 4f, -z, x + 1f / 4f },
				{ -y, z + 3f / 4f, x + 3f / 4f }, { y + 3f / 4f, z + 1f / 4f, -x + 1f / 2f },
				{ -y + 3f / 4f, -x + 1f / 4f, z + 1f / 2f }, { y + 1f / 2f, x + 1f / 2f, z },
				{ -y + 1f / 4f, x, -z + 1f / 4f }, { y, -x + 3f / 4f, -z + 3f / 4f },
				{ -x + 3f / 4f, -z + 1f / 4f, y + 1f / 2f }, { x, -z + 3f / 4f, -y + 3f / 4f },
				{ x + 1f / 2f, z + 1f / 2f, y }, { -x + 1f / 4f, z, -y + 1f / 4f },
				{ -z + 3f / 4f, -y + 1f / 4f, x + 1f / 2f }, { -z + 1f / 4f, y, -x + 1f / 4f },
				{ z, -y + 3f / 4f, -x + 3f / 4f }, { z + 1f / 2f, y + 1f / 2f, x }, };
	}

	public static float[][] pos228(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, -z },
				{ x + 1f / 2f, -y, -z + 1f / 2f }, { z, x, y }, { z + 1f / 2f, -x, -y + 1f / 2f },
				{ -z, -x + 1f / 2f, y + 1f / 2f }, { -z + 1f / 2f, x + 1f / 2f, -y }, { y, z, x },
				{ -y + 1f / 2f, z + 1f / 2f, -x }, { y + 1f / 2f, -z, -x + 1f / 2f }, { -y, -z + 1f / 2f, x + 1f / 2f },
				{ y + 3f / 4f, x + 1f / 4f, -z + 3f / 4f }, { -y + 1f / 4f, -x + 1f / 4f, -z + 1f / 4f },
				{ y + 1f / 4f, -x + 3f / 4f, z + 3f / 4f }, { -y + 3f / 4f, x + 3f / 4f, z + 1f / 4f },
				{ x + 3f / 4f, z + 1f / 4f, -y + 3f / 4f }, { -x + 3f / 4f, z + 3f / 4f, y + 1f / 4f },
				{ -x + 1f / 4f, -z + 1f / 4f, -y + 1f / 4f }, { x + 1f / 4f, -z + 3f / 4f, y + 3f / 4f },
				{ z + 3f / 4f, y + 1f / 4f, -x + 3f / 4f }, { z + 1f / 4f, -y + 3f / 4f, x + 3f / 4f },
				{ -z + 3f / 4f, y + 3f / 4f, x + 1f / 4f }, { -z + 1f / 4f, -y + 1f / 4f, -x + 1f / 4f },
				{ -x + 3f / 4f, -y + 3f / 4f, -z + 3f / 4f }, { x + 3f / 4f, y + 1f / 4f, -z + 1f / 4f },
				{ x + 1f / 4f, -y + 1f / 4f, z + 3f / 4f }, { -x + 1f / 4f, y + 3f / 4f, z + 1f / 4f },
				{ -z + 3f / 4f, -x + 3f / 4f, -y + 3f / 4f }, { -z + 1f / 4f, x + 3f / 4f, y + 1f / 4f },
				{ z + 3f / 4f, x + 1f / 4f, -y + 1f / 4f }, { z + 1f / 4f, -x + 1f / 4f, y + 3f / 4f },
				{ -y + 3f / 4f, -z + 3f / 4f, -x + 3f / 4f }, { y + 1f / 4f, -z + 1f / 4f, x + 3f / 4f },
				{ -y + 1f / 4f, z + 3f / 4f, x + 1f / 4f }, { y + 3f / 4f, z + 1f / 4f, -x + 1f / 4f },
				{ -y, -x + 1f / 2f, z }, { y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, x, -z },
				{ y, -x, -z + 1f / 2f }, { -x, -z + 1f / 2f, y }, { x, -z, -y + 1f / 2f },
				{ x + 1f / 2f, z + 1f / 2f, y + 1f / 2f }, { -x + 1f / 2f, z, -y }, { -z, -y + 1f / 2f, x },
				{ -z + 1f / 2f, y, -x }, { z, -y, -x + 1f / 2f }, { z + 1f / 2f, y + 1f / 2f, x + 1f / 2f },
				{ x, y + 1f / 2f, z + 1f / 2f }, { -x, -y, z }, { -x + 1f / 2f, y, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z, x + 1f / 2f, y + 1f / 2f }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z, -x, y }, { -z + 1f / 2f, x, -y + 1f / 2f }, { y, z + 1f / 2f, x + 1f / 2f },
				{ -y + 1f / 2f, z, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y, -z, x },
				{ y + 3f / 4f, x + 3f / 4f, -z + 1f / 4f }, { -y + 1f / 4f, -x + 3f / 4f, -z + 3f / 4f },
				{ y + 1f / 4f, -x + 1f / 4f, z + 1f / 4f }, { -y + 3f / 4f, x + 1f / 4f, z + 3f / 4f },
				{ x + 3f / 4f, z + 3f / 4f, -y + 1f / 4f }, { -x + 3f / 4f, z + 1f / 4f, y + 3f / 4f },
				{ -x + 1f / 4f, -z + 3f / 4f, -y + 3f / 4f }, { x + 1f / 4f, -z + 1f / 4f, y + 1f / 4f },
				{ z + 3f / 4f, y + 3f / 4f, -x + 1f / 4f }, { z + 1f / 4f, -y + 1f / 4f, x + 1f / 4f },
				{ -z + 3f / 4f, y + 1f / 4f, x + 3f / 4f }, { -z + 1f / 4f, -y + 3f / 4f, -x + 3f / 4f },
				{ -x + 3f / 4f, -y + 1f / 4f, -z + 1f / 4f }, { x + 3f / 4f, y + 3f / 4f, -z + 3f / 4f },
				{ x + 1f / 4f, -y + 3f / 4f, z + 1f / 4f }, { -x + 1f / 4f, y + 1f / 4f, z + 3f / 4f },
				{ -z + 3f / 4f, -x + 1f / 4f, -y + 1f / 4f }, { -z + 1f / 4f, x + 1f / 4f, y + 3f / 4f },
				{ z + 3f / 4f, x + 3f / 4f, -y + 3f / 4f }, { z + 1f / 4f, -x + 3f / 4f, y + 1f / 4f },
				{ -y + 3f / 4f, -z + 1f / 4f, -x + 1f / 4f }, { y + 1f / 4f, -z + 3f / 4f, x + 1f / 4f },
				{ -y + 1f / 4f, z + 1f / 4f, x + 3f / 4f }, { y + 3f / 4f, z + 3f / 4f, -x + 3f / 4f },
				{ -y, -x, z + 1f / 2f }, { y + 1f / 2f, x, z }, { -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f },
				{ y, -x + 1f / 2f, -z }, { -x, -z, y + 1f / 2f }, { x, -z + 1f / 2f, -y }, { x + 1f / 2f, z, y },
				{ -x + 1f / 2f, z + 1f / 2f, -y + 1f / 2f }, { -z, -y, x + 1f / 2f },
				{ -z + 1f / 2f, y + 1f / 2f, -x + 1f / 2f }, { z, -y + 1f / 2f, -x }, { z + 1f / 2f, y, x },
				{ x + 1f / 2f, y, z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, z }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x, -y, -z }, { z + 1f / 2f, x, y + 1f / 2f }, { z, -x, -y }, { -z + 1f / 2f, -x + 1f / 2f, y },
				{ -z, x + 1f / 2f, -y + 1f / 2f }, { y + 1f / 2f, z, x + 1f / 2f }, { -y, z + 1f / 2f, -x + 1f / 2f },
				{ y, -z, -x }, { -y + 1f / 2f, -z + 1f / 2f, x }, { y + 1f / 4f, x + 1f / 4f, -z + 1f / 4f },
				{ -y + 3f / 4f, -x + 1f / 4f, -z + 3f / 4f }, { y + 3f / 4f, -x + 3f / 4f, z + 1f / 4f },
				{ -y + 1f / 4f, x + 3f / 4f, z + 3f / 4f }, { x + 1f / 4f, z + 1f / 4f, -y + 1f / 4f },
				{ -x + 1f / 4f, z + 3f / 4f, y + 3f / 4f }, { -x + 3f / 4f, -z + 1f / 4f, -y + 3f / 4f },
				{ x + 3f / 4f, -z + 3f / 4f, y + 1f / 4f }, { z + 1f / 4f, y + 1f / 4f, -x + 1f / 4f },
				{ z + 3f / 4f, -y + 3f / 4f, x + 1f / 4f }, { -z + 1f / 4f, y + 3f / 4f, x + 3f / 4f },
				{ -z + 3f / 4f, -y + 1f / 4f, -x + 3f / 4f }, { -x + 1f / 4f, -y + 3f / 4f, -z + 1f / 4f },
				{ x + 1f / 4f, y + 1f / 4f, -z + 3f / 4f }, { x + 3f / 4f, -y + 1f / 4f, z + 1f / 4f },
				{ -x + 3f / 4f, y + 3f / 4f, z + 3f / 4f }, { -z + 1f / 4f, -x + 3f / 4f, -y + 1f / 4f },
				{ -z + 3f / 4f, x + 3f / 4f, y + 3f / 4f }, { z + 1f / 4f, x + 1f / 4f, -y + 3f / 4f },
				{ z + 3f / 4f, -x + 1f / 4f, y + 1f / 4f }, { -y + 1f / 4f, -z + 3f / 4f, -x + 1f / 4f },
				{ y + 3f / 4f, -z + 1f / 4f, x + 1f / 4f }, { -y + 3f / 4f, z + 3f / 4f, x + 3f / 4f },
				{ y + 1f / 4f, z + 1f / 4f, -x + 3f / 4f }, { -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f },
				{ y, x + 1f / 2f, z }, { -y, x, -z + 1f / 2f }, { y + 1f / 2f, -x, -z },
				{ -x + 1f / 2f, -z + 1f / 2f, y + 1f / 2f }, { x + 1f / 2f, -z, -y }, { x, z + 1f / 2f, y },
				{ -x, z, -y + 1f / 2f }, { -z + 1f / 2f, -y + 1f / 2f, x + 1f / 2f }, { -z, y, -x + 1f / 2f },
				{ z + 1f / 2f, -y, -x }, { z, y + 1f / 2f, x }, { x + 1f / 2f, y + 1f / 2f, z },
				{ -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y, -z }, { x, -y + 1f / 2f, -z + 1f / 2f },
				{ z + 1f / 2f, x + 1f / 2f, y }, { z, -x + 1f / 2f, -y + 1f / 2f }, { -z + 1f / 2f, -x, y + 1f / 2f },
				{ -z, x, -y }, { y + 1f / 2f, z + 1f / 2f, x }, { -y, z, -x }, { y, -z + 1f / 2f, -x + 1f / 2f },
				{ -y + 1f / 2f, -z, x + 1f / 2f }, { y + 1f / 4f, x + 3f / 4f, -z + 3f / 4f },
				{ -y + 3f / 4f, -x + 3f / 4f, -z + 1f / 4f }, { y + 3f / 4f, -x + 1f / 4f, z + 3f / 4f },
				{ -y + 1f / 4f, x + 1f / 4f, z + 1f / 4f }, { x + 1f / 4f, z + 3f / 4f, -y + 3f / 4f },
				{ -x + 1f / 4f, z + 1f / 4f, y + 1f / 4f }, { -x + 3f / 4f, -z + 3f / 4f, -y + 1f / 4f },
				{ x + 3f / 4f, -z + 1f / 4f, y + 3f / 4f }, { z + 1f / 4f, y + 3f / 4f, -x + 3f / 4f },
				{ z + 3f / 4f, -y + 1f / 4f, x + 3f / 4f }, { -z + 1f / 4f, y + 1f / 4f, x + 1f / 4f },
				{ -z + 3f / 4f, -y + 3f / 4f, -x + 1f / 4f }, { -x + 1f / 4f, -y + 1f / 4f, -z + 3f / 4f },
				{ x + 1f / 4f, y + 3f / 4f, -z + 1f / 4f }, { x + 3f / 4f, -y + 3f / 4f, z + 3f / 4f },
				{ -x + 3f / 4f, y + 1f / 4f, z + 1f / 4f }, { -z + 1f / 4f, -x + 1f / 4f, -y + 3f / 4f },
				{ -z + 3f / 4f, x + 1f / 4f, y + 1f / 4f }, { z + 1f / 4f, x + 3f / 4f, -y + 1f / 4f },
				{ z + 3f / 4f, -x + 3f / 4f, y + 3f / 4f }, { -y + 1f / 4f, -z + 1f / 4f, -x + 3f / 4f },
				{ y + 3f / 4f, -z + 3f / 4f, x + 3f / 4f }, { -y + 3f / 4f, z + 1f / 4f, x + 1f / 4f },
				{ y + 1f / 4f, z + 3f / 4f, -x + 1f / 4f }, { -y + 1f / 2f, -x, z }, { y, x, z + 1f / 2f },
				{ -y, x + 1f / 2f, -z }, { y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f }, { -x + 1f / 2f, -z, y },
				{ x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f }, { x, z, y + 1f / 2f }, { -x, z + 1f / 2f, -y },
				{ -z + 1f / 2f, -y, x }, { -z, y + 1f / 2f, -x }, { z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f },
				{ z, y, x + 1f / 2f }, };
	}

	public static float[][] pos228alt(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 4f, -y + 3f / 4f, z + 1f / 2f },
				{ -x + 3f / 4f, y + 1f / 2f, -z + 1f / 4f }, { x + 1f / 2f, -y + 1f / 4f, -z + 3f / 4f }, { z, x, y },
				{ z + 1f / 2f, -x + 1f / 4f, -y + 3f / 4f }, { -z + 1f / 4f, -x + 3f / 4f, y + 1f / 2f },
				{ -z + 3f / 4f, x + 1f / 2f, -y + 1f / 4f }, { y, z, x }, { -y + 3f / 4f, z + 1f / 2f, -x + 1f / 4f },
				{ y + 1f / 2f, -z + 1f / 4f, -x + 3f / 4f }, { -y + 1f / 4f, -z + 3f / 4f, x + 1f / 2f },
				{ y + 3f / 4f, x + 1f / 4f, -z }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ y + 1f / 4f, -x, z + 3f / 4f }, { -y, x + 3f / 4f, z + 1f / 4f }, { x + 3f / 4f, z + 1f / 4f, -y },
				{ -x, z + 3f / 4f, y + 1f / 4f }, { -x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f },
				{ x + 1f / 4f, -z, y + 3f / 4f }, { z + 3f / 4f, y + 1f / 4f, -x }, { z + 1f / 4f, -y, x + 3f / 4f },
				{ -z, y + 3f / 4f, x + 1f / 4f }, { -z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f }, { -x, -y, -z },
				{ x + 3f / 4f, y + 1f / 4f, -z + 1f / 2f }, { x + 1f / 4f, -y + 1f / 2f, z + 3f / 4f },
				{ -x + 1f / 2f, y + 3f / 4f, z + 1f / 4f }, { -z, -x, -y }, { -z + 1f / 2f, x + 3f / 4f, y + 1f / 4f },
				{ z + 3f / 4f, x + 1f / 4f, -y + 1f / 2f }, { z + 1f / 4f, -x + 1f / 2f, y + 3f / 4f }, { -y, -z, -x },
				{ y + 1f / 4f, -z + 1f / 2f, x + 3f / 4f }, { -y + 1f / 2f, z + 3f / 4f, x + 1f / 4f },
				{ y + 3f / 4f, z + 1f / 4f, -x + 1f / 2f }, { -y + 1f / 4f, -x + 3f / 4f, z },
				{ y + 1f / 2f, x + 1f / 2f, z + 1f / 2f }, { -y + 3f / 4f, x, -z + 1f / 4f },
				{ y, -x + 1f / 4f, -z + 3f / 4f }, { -x + 1f / 4f, -z + 3f / 4f, y }, { x, -z + 1f / 4f, -y + 3f / 4f },
				{ x + 1f / 2f, z + 1f / 2f, y + 1f / 2f }, { -x + 3f / 4f, z, -y + 1f / 4f },
				{ -z + 1f / 4f, -y + 3f / 4f, x }, { -z + 3f / 4f, y, -x + 1f / 4f }, { z, -y + 1f / 4f, -x + 3f / 4f },
				{ z + 1f / 2f, y + 1f / 2f, x + 1f / 2f }, { x, y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 4f, -y + 1f / 4f, z }, { -x + 3f / 4f, y, -z + 3f / 4f },
				{ x + 1f / 2f, -y + 3f / 4f, -z + 1f / 4f }, { z, x + 1f / 2f, y + 1f / 2f },
				{ z + 1f / 2f, -x + 3f / 4f, -y + 1f / 4f }, { -z + 1f / 4f, -x + 1f / 4f, y },
				{ -z + 3f / 4f, x, -y + 3f / 4f }, { y, z + 1f / 2f, x + 1f / 2f }, { -y + 3f / 4f, z, -x + 3f / 4f },
				{ y + 1f / 2f, -z + 3f / 4f, -x + 1f / 4f }, { -y + 1f / 4f, -z + 1f / 4f, x },
				{ y + 3f / 4f, x + 3f / 4f, -z + 1f / 2f }, { -y + 1f / 2f, -x, -z },
				{ y + 1f / 4f, -x + 1f / 2f, z + 1f / 4f }, { -y, x + 1f / 4f, z + 3f / 4f },
				{ x + 3f / 4f, z + 3f / 4f, -y + 1f / 2f }, { -x, z + 1f / 4f, y + 3f / 4f }, { -x + 1f / 2f, -z, -y },
				{ x + 1f / 4f, -z + 1f / 2f, y + 1f / 4f }, { z + 3f / 4f, y + 3f / 4f, -x + 1f / 2f },
				{ z + 1f / 4f, -y + 1f / 2f, x + 1f / 4f }, { -z, y + 1f / 4f, x + 3f / 4f }, { -z + 1f / 2f, -y, -x },
				{ -x, -y + 1f / 2f, -z + 1f / 2f }, { x + 3f / 4f, y + 3f / 4f, -z }, { x + 1f / 4f, -y, z + 1f / 4f },
				{ -x + 1f / 2f, y + 1f / 4f, z + 3f / 4f }, { -z, -x + 1f / 2f, -y + 1f / 2f },
				{ -z + 1f / 2f, x + 1f / 4f, y + 3f / 4f }, { z + 3f / 4f, x + 3f / 4f, -y },
				{ z + 1f / 4f, -x, y + 1f / 4f }, { -y, -z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 4f, -z, x + 1f / 4f },
				{ -y + 1f / 2f, z + 1f / 4f, x + 3f / 4f }, { y + 3f / 4f, z + 3f / 4f, -x },
				{ -y + 1f / 4f, -x + 1f / 4f, z + 1f / 2f }, { y + 1f / 2f, x, z },
				{ -y + 3f / 4f, x + 1f / 2f, -z + 3f / 4f }, { y, -x + 3f / 4f, -z + 1f / 4f },
				{ -x + 1f / 4f, -z + 1f / 4f, y + 1f / 2f }, { x, -z + 3f / 4f, -y + 1f / 4f }, { x + 1f / 2f, z, y },
				{ -x + 3f / 4f, z + 1f / 2f, -y + 3f / 4f }, { -z + 1f / 4f, -y + 1f / 4f, x + 1f / 2f },
				{ -z + 3f / 4f, y + 1f / 2f, -x + 3f / 4f }, { z, -y + 3f / 4f, -x + 1f / 4f }, { z + 1f / 2f, y, x },
				{ x + 1f / 2f, y, z + 1f / 2f }, { -x + 3f / 4f, -y + 3f / 4f, z },
				{ -x + 1f / 4f, y + 1f / 2f, -z + 3f / 4f }, { x, -y + 1f / 4f, -z + 1f / 4f },
				{ z + 1f / 2f, x, y + 1f / 2f }, { z, -x + 1f / 4f, -y + 1f / 4f }, { -z + 3f / 4f, -x + 3f / 4f, y },
				{ -z + 1f / 4f, x + 1f / 2f, -y + 3f / 4f }, { y + 1f / 2f, z, x + 1f / 2f },
				{ -y + 1f / 4f, z + 1f / 2f, -x + 3f / 4f }, { y, -z + 1f / 4f, -x + 1f / 4f },
				{ -y + 3f / 4f, -z + 3f / 4f, x }, { y + 1f / 4f, x + 1f / 4f, -z + 1f / 2f }, { -y, -x + 1f / 2f, -z },
				{ y + 3f / 4f, -x, z + 1f / 4f }, { -y + 1f / 2f, x + 3f / 4f, z + 3f / 4f },
				{ x + 1f / 4f, z + 1f / 4f, -y + 1f / 2f }, { -x + 1f / 2f, z + 3f / 4f, y + 3f / 4f },
				{ -x, -z + 1f / 2f, -y }, { x + 3f / 4f, -z, y + 1f / 4f }, { z + 1f / 4f, y + 1f / 4f, -x + 1f / 2f },
				{ z + 3f / 4f, -y, x + 1f / 4f }, { -z + 1f / 2f, y + 3f / 4f, x + 3f / 4f }, { -z, -y + 1f / 2f, -x },
				{ -x + 1f / 2f, -y, -z + 1f / 2f }, { x + 1f / 4f, y + 1f / 4f, -z },
				{ x + 3f / 4f, -y + 1f / 2f, z + 1f / 4f }, { -x, y + 3f / 4f, z + 3f / 4f },
				{ -z + 1f / 2f, -x, -y + 1f / 2f }, { -z, x + 3f / 4f, y + 3f / 4f }, { z + 1f / 4f, x + 1f / 4f, -y },
				{ z + 3f / 4f, -x + 1f / 2f, y + 1f / 4f }, { -y + 1f / 2f, -z, -x + 1f / 2f },
				{ y + 3f / 4f, -z + 1f / 2f, x + 1f / 4f }, { -y, z + 3f / 4f, x + 3f / 4f },
				{ y + 1f / 4f, z + 1f / 4f, -x }, { -y + 3f / 4f, -x + 3f / 4f, z + 1f / 2f }, { y, x + 1f / 2f, z },
				{ -y + 1f / 4f, x, -z + 3f / 4f }, { y + 1f / 2f, -x + 1f / 4f, -z + 1f / 4f },
				{ -x + 3f / 4f, -z + 3f / 4f, y + 1f / 2f }, { x + 1f / 2f, -z + 1f / 4f, -y + 1f / 4f },
				{ x, z + 1f / 2f, y }, { -x + 1f / 4f, z, -y + 3f / 4f }, { -z + 3f / 4f, -y + 3f / 4f, x + 1f / 2f },
				{ -z + 1f / 4f, y, -x + 3f / 4f }, { z + 1f / 2f, -y + 1f / 4f, -x + 1f / 4f }, { z, y + 1f / 2f, x },
				{ x + 1f / 2f, y + 1f / 2f, z }, { -x + 3f / 4f, -y + 1f / 4f, z + 1f / 2f },
				{ -x + 1f / 4f, y, -z + 1f / 4f }, { x, -y + 3f / 4f, -z + 3f / 4f }, { z + 1f / 2f, x + 1f / 2f, y },
				{ z, -x + 3f / 4f, -y + 3f / 4f }, { -z + 3f / 4f, -x + 1f / 4f, y + 1f / 2f },
				{ -z + 1f / 4f, x, -y + 1f / 4f }, { y + 1f / 2f, z + 1f / 2f, x }, { -y + 1f / 4f, z, -x + 1f / 4f },
				{ y, -z + 3f / 4f, -x + 3f / 4f }, { -y + 3f / 4f, -z + 1f / 4f, x + 1f / 2f },
				{ y + 1f / 4f, x + 3f / 4f, -z }, { -y, -x, -z + 1f / 2f }, { y + 3f / 4f, -x + 1f / 2f, z + 3f / 4f },
				{ -y + 1f / 2f, x + 1f / 4f, z + 1f / 4f }, { x + 1f / 4f, z + 3f / 4f, -y },
				{ -x + 1f / 2f, z + 1f / 4f, y + 1f / 4f }, { -x, -z, -y + 1f / 2f },
				{ x + 3f / 4f, -z + 1f / 2f, y + 3f / 4f }, { z + 1f / 4f, y + 3f / 4f, -x },
				{ z + 3f / 4f, -y + 1f / 2f, x + 3f / 4f }, { -z + 1f / 2f, y + 1f / 4f, x + 1f / 4f },
				{ -z, -y, -x + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, -z },
				{ x + 1f / 4f, y + 3f / 4f, -z + 1f / 2f }, { x + 3f / 4f, -y, z + 3f / 4f },
				{ -x, y + 1f / 4f, z + 1f / 4f }, { -z + 1f / 2f, -x + 1f / 2f, -y }, { -z, x + 1f / 4f, y + 1f / 4f },
				{ z + 1f / 4f, x + 3f / 4f, -y + 1f / 2f }, { z + 3f / 4f, -x, y + 3f / 4f },
				{ -y + 1f / 2f, -z + 1f / 2f, -x }, { y + 3f / 4f, -z, x + 3f / 4f }, { -y, z + 1f / 4f, x + 1f / 4f },
				{ y + 1f / 4f, z + 3f / 4f, -x + 1f / 2f }, { -y + 3f / 4f, -x + 1f / 4f, z }, { y, x, z + 1f / 2f },
				{ -y + 1f / 4f, x + 1f / 2f, -z + 1f / 4f }, { y + 1f / 2f, -x + 3f / 4f, -z + 3f / 4f },
				{ -x + 3f / 4f, -z + 1f / 4f, y }, { x + 1f / 2f, -z + 3f / 4f, -y + 3f / 4f }, { x, z, y + 1f / 2f },
				{ -x + 1f / 4f, z + 1f / 2f, -y + 1f / 4f }, { -z + 3f / 4f, -y + 1f / 4f, x },
				{ -z + 1f / 4f, y + 1f / 2f, -x + 1f / 4f }, { z + 1f / 2f, -y + 3f / 4f, -x + 3f / 4f },
				{ z, y, x + 1f / 2f }, };
	}

	public static float[][] pos229(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x, -y, z }, { -x, y, -z }, { x, -y, -z }, { z, x, y }, { z, -x, -y },
				{ -z, -x, y }, { -z, x, -y }, { y, z, x }, { -y, z, -x }, { y, -z, -x }, { -y, -z, x }, { y, x, -z },
				{ -y, -x, -z }, { y, -x, z }, { -y, x, z }, { x, z, -y }, { -x, z, y }, { -x, -z, -y }, { x, -z, y },
				{ z, y, -x }, { z, -y, x }, { -z, y, x }, { -z, -y, -x }, { -x, -y, -z }, { x, y, -z }, { x, -y, z },
				{ -x, y, z }, { -z, -x, -y }, { -z, x, y }, { z, x, -y }, { z, -x, y }, { -y, -z, -x }, { y, -z, x },
				{ -y, z, x }, { y, z, -x }, { -y, -x, z }, { y, x, z }, { -y, x, -z }, { y, -x, -z }, { -x, -z, y },
				{ x, -z, -y }, { x, z, y }, { -x, z, -y }, { -z, -y, x }, { -z, y, -x }, { z, -y, -x }, { z, y, x },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f },
				{ z + 1f / 2f, x + 1f / 2f, y + 1f / 2f }, { z + 1f / 2f, -x + 1f / 2f, -y + 1f / 2f },
				{ -z + 1f / 2f, -x + 1f / 2f, y + 1f / 2f }, { -z + 1f / 2f, x + 1f / 2f, -y + 1f / 2f },
				{ y + 1f / 2f, z + 1f / 2f, x + 1f / 2f }, { -y + 1f / 2f, z + 1f / 2f, -x + 1f / 2f },
				{ y + 1f / 2f, -z + 1f / 2f, -x + 1f / 2f }, { -y + 1f / 2f, -z + 1f / 2f, x + 1f / 2f },
				{ y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { -y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { -y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ x + 1f / 2f, z + 1f / 2f, -y + 1f / 2f }, { -x + 1f / 2f, z + 1f / 2f, y + 1f / 2f },
				{ -x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f }, { x + 1f / 2f, -z + 1f / 2f, y + 1f / 2f },
				{ z + 1f / 2f, y + 1f / 2f, -x + 1f / 2f }, { z + 1f / 2f, -y + 1f / 2f, x + 1f / 2f },
				{ -z + 1f / 2f, y + 1f / 2f, x + 1f / 2f }, { -z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x + 1f / 2f, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, z + 1f / 2f }, { -x + 1f / 2f, y + 1f / 2f, z + 1f / 2f },
				{ -z + 1f / 2f, -x + 1f / 2f, -y + 1f / 2f }, { -z + 1f / 2f, x + 1f / 2f, y + 1f / 2f },
				{ z + 1f / 2f, x + 1f / 2f, -y + 1f / 2f }, { z + 1f / 2f, -x + 1f / 2f, y + 1f / 2f },
				{ -y + 1f / 2f, -z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, x + 1f / 2f },
				{ -y + 1f / 2f, z + 1f / 2f, x + 1f / 2f }, { y + 1f / 2f, z + 1f / 2f, -x + 1f / 2f },
				{ -y + 1f / 2f, -x + 1f / 2f, z + 1f / 2f }, { y + 1f / 2f, x + 1f / 2f, z + 1f / 2f },
				{ -y + 1f / 2f, x + 1f / 2f, -z + 1f / 2f }, { y + 1f / 2f, -x + 1f / 2f, -z + 1f / 2f },
				{ -x + 1f / 2f, -z + 1f / 2f, y + 1f / 2f }, { x + 1f / 2f, -z + 1f / 2f, -y + 1f / 2f },
				{ x + 1f / 2f, z + 1f / 2f, y + 1f / 2f }, { -x + 1f / 2f, z + 1f / 2f, -y + 1f / 2f },
				{ -z + 1f / 2f, -y + 1f / 2f, x + 1f / 2f }, { -z + 1f / 2f, y + 1f / 2f, -x + 1f / 2f },
				{ z + 1f / 2f, -y + 1f / 2f, -x + 1f / 2f }, { z + 1f / 2f, y + 1f / 2f, x + 1f / 2f }, };
	}

	public static float[][] pos230(float x, float y, float z) {
		return new float[][] { { x, y, z }, { -x + 1f / 2f, -y, z + 1f / 2f }, { -x, y + 1f / 2f, -z + 1f / 2f },
				{ x + 1f / 2f, -y + 1f / 2f, -z }, { z, x, y }, { z + 1f / 2f, -x + 1f / 2f, -y },
				{ -z + 1f / 2f, -x, y + 1f / 2f }, { -z, x + 1f / 2f, -y + 1f / 2f }, { y, z, x },
				{ -y, z + 1f / 2f, -x + 1f / 2f }, { y + 1f / 2f, -z + 1f / 2f, -x }, { -y + 1f / 2f, -z, x + 1f / 2f },
				{ y + 3f / 4f, x + 1f / 4f, -z + 1f / 4f }, { -y + 3f / 4f, -x + 3f / 4f, -z + 3f / 4f },
				{ y + 1f / 4f, -x + 1f / 4f, z + 3f / 4f }, { -y + 1f / 4f, x + 3f / 4f, z + 1f / 4f },
				{ x + 3f / 4f, z + 1f / 4f, -y + 1f / 4f }, { -x + 1f / 4f, z + 3f / 4f, y + 1f / 4f },
				{ -x + 3f / 4f, -z + 3f / 4f, -y + 3f / 4f }, { x + 1f / 4f, -z + 1f / 4f, y + 3f / 4f },
				{ z + 3f / 4f, y + 1f / 4f, -x + 1f / 4f }, { z + 1f / 4f, -y + 1f / 4f, x + 3f / 4f },
				{ -z + 1f / 4f, y + 3f / 4f, x + 1f / 4f }, { -z + 3f / 4f, -y + 3f / 4f, -x + 3f / 4f },
				{ -x, -y, -z }, { x + 1f / 2f, y, -z + 1f / 2f }, { x, -y + 1f / 2f, z + 1f / 2f },
				{ -x + 1f / 2f, y + 1f / 2f, z }, { -z, -x, -y }, { -z + 1f / 2f, x + 1f / 2f, y },
				{ z + 1f / 2f, x, -y + 1f / 2f }, { z, -x + 1f / 2f, y + 1f / 2f }, { -y, -z, -x },
				{ y, -z + 1f / 2f, x + 1f / 2f }, { -y + 1f / 2f, z + 1f / 2f, x }, { y + 1f / 2f, z, -x + 1f / 2f },
				{ -y + 1f / 4f, -x + 3f / 4f, z + 3f / 4f }, { y + 1f / 4f, x + 1f / 4f, z + 1f / 4f },
				{ -y + 3f / 4f, x + 3f / 4f, -z + 1f / 4f }, { y + 3f / 4f, -x + 1f / 4f, -z + 3f / 4f },
				{ -x + 1f / 4f, -z + 3f / 4f, y + 3f / 4f }, { x + 3f / 4f, -z + 1f / 4f, -y + 3f / 4f },
				{ x + 1f / 4f, z + 1f / 4f, y + 1f / 4f }, { -x + 3f / 4f, z + 3f / 4f, -y + 1f / 4f },
				{ -z + 1f / 4f, -y + 3f / 4f, x + 3f / 4f }, { -z + 3f / 4f, y + 3f / 4f, -x + 1f / 4f },
				{ z + 3f / 4f, -y + 1f / 4f, -x + 3f / 4f }, { z + 1f / 4f, y + 1f / 4f, x + 1f / 4f },
				{ x + 1f / 2f, y + 1f / 2f, z + 1f / 2f }, { -x, -y + 1f / 2f, z }, { -x + 1f / 2f, y, -z },
				{ x, -y, -z + 1f / 2f }, { z + 1f / 2f, x + 1f / 2f, y + 1f / 2f }, { z, -x, -y + 1f / 2f },
				{ -z, -x + 1f / 2f, y }, { -z + 1f / 2f, x, -y }, { y + 1f / 2f, z + 1f / 2f, x + 1f / 2f },
				{ -y + 1f / 2f, z, -x }, { y, -z, -x + 1f / 2f }, { -y, -z + 1f / 2f, x },
				{ y + 1f / 4f, x + 3f / 4f, -z + 3f / 4f }, { -y + 1f / 4f, -x + 1f / 4f, -z + 1f / 4f },
				{ y + 3f / 4f, -x + 3f / 4f, z + 1f / 4f }, { -y + 3f / 4f, x + 1f / 4f, z + 3f / 4f },
				{ x + 1f / 4f, z + 3f / 4f, -y + 3f / 4f }, { -x + 3f / 4f, z + 1f / 4f, y + 3f / 4f },
				{ -x + 1f / 4f, -z + 1f / 4f, -y + 1f / 4f }, { x + 3f / 4f, -z + 3f / 4f, y + 1f / 4f },
				{ z + 1f / 4f, y + 3f / 4f, -x + 3f / 4f }, { z + 3f / 4f, -y + 3f / 4f, x + 1f / 4f },
				{ -z + 3f / 4f, y + 1f / 4f, x + 3f / 4f }, { -z + 1f / 4f, -y + 1f / 4f, -x + 1f / 4f },
				{ -x + 1f / 2f, -y + 1f / 2f, -z + 1f / 2f }, { x, y + 1f / 2f, -z }, { x + 1f / 2f, -y, z },
				{ -x, y, z + 1f / 2f }, { -z + 1f / 2f, -x + 1f / 2f, -y + 1f / 2f }, { -z, x, y + 1f / 2f },
				{ z, x + 1f / 2f, -y }, { z + 1f / 2f, -x, y }, { -y + 1f / 2f, -z + 1f / 2f, -x + 1f / 2f },
				{ y + 1f / 2f, -z, x }, { -y, z, x + 1f / 2f }, { y, z + 1f / 2f, -x },
				{ -y + 3f / 4f, -x + 1f / 4f, z + 1f / 4f }, { y + 3f / 4f, x + 3f / 4f, z + 3f / 4f },
				{ -y + 1f / 4f, x + 1f / 4f, -z + 3f / 4f }, { y + 1f / 4f, -x + 3f / 4f, -z + 1f / 4f },
				{ -x + 3f / 4f, -z + 1f / 4f, y + 1f / 4f }, { x + 1f / 4f, -z + 3f / 4f, -y + 1f / 4f },
				{ x + 3f / 4f, z + 3f / 4f, y + 3f / 4f }, { -x + 1f / 4f, z + 1f / 4f, -y + 3f / 4f },
				{ -z + 3f / 4f, -y + 1f / 4f, x + 1f / 4f }, { -z + 1f / 4f, y + 1f / 4f, -x + 3f / 4f },
				{ z + 1f / 4f, -y + 3f / 4f, -x + 1f / 4f }, { z + 3f / 4f, y + 3f / 4f, x + 3f / 4f }, };
	}

	static Method[] methods;

	static {
		try {
			methods = new Method[] {
					CellGen.class.getMethod("pos1", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos4", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos5", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos6", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos7", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos8", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos9", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos10", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos11", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos12", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos13", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos14", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos15", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos16", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos17", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos18", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos19", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos20", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos21", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos22", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos23", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos24", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos25", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos26", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos27", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos28", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos29", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos30", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos31", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos32", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos33", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos34", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos35", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos36", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos37", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos38", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos39", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos40", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos41", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos42", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos43", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos44", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos45", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos46", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos47", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos48", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos49", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos50", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos51", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos52", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos53", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos54", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos55", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos56", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos57", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos58", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos59", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos60", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos61", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos62", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos63", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos64", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos65", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos66", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos67", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos68", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos69", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos70", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos71", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos72", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos73", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos74", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos75", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos76", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos77", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos78", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos79", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos80", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos81", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos82", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos83", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos84", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos85", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos86", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos87", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos88", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos89", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos90", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos91", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos92", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos93", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos94", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos95", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos96", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos97", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos98", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos99", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos100", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos101", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos102", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos103", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos104", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos105", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos106", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos107", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos108", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos109", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos110", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos111", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos112", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos113", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos114", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos115", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos116", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos117", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos118", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos119", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos120", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos121", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos122", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos123", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos124", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos125", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos126", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos127", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos128", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos129", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos130", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos131", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos132", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos133", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos134", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos135", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos136", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos137", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos138", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos139", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos140", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos141", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos142", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos143", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos144", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos145", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos146", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos147", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos148", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos149", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos150", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos151", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos152", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos153", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos154", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos155", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos156", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos157", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos158", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos159", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos160", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos161", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos162", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos163", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos164", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos165", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos166", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos167", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos168", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos169", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos170", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos171", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos172", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos173", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos174", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos175", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos176", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos177", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos178", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos179", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos180", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos181", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos182", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos183", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos184", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos185", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos186", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos187", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos188", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos189", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos190", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos191", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos192", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos193", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos194", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos195", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos196", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos197", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos198", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos199", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos200", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos201", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos202", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos203", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos204", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos205", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos206", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos207", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos208", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos209", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos210", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos211", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos212", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos213", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos214", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos215", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos216", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos217", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos218", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos219", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos220", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos221", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos222", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos223", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos224", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos225", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos226", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos227", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos228", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos229", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos230", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos3alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos4alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos5alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos6alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos7alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos8alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos9alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos10alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos11alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos12alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos13alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos14alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos15alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos48alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos50alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos59alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos68alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos70alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos85alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos86alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos88alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos125alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos126alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos129alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos130alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos133alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos134alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos137alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos138alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos141alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos142alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos146alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos148alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos155alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos160alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos161alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos166alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos167alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos201alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos203alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos222alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos224alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos227alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos228alt", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos5_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos5_3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos5alt_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos5alt_3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos7_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos7_3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos7alt_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos7alt_3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos8_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos8_3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos8alt_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos8alt_3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos9_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos9_3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos9alt_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos9alt_3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos12_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos12_3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos12alt_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos12alt_3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos13_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos13_3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos13alt_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos13alt_3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos14_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos14_3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos14alt_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos14alt_3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos15_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos15_3", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos15alt_2", new Class[] { float.class, float.class, float.class }),
					CellGen.class.getMethod("pos15alt_3", new Class[] { float.class, float.class, float.class }),

			};
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public static float[][] pos(int n, int c, float x, float y, float z) {
		try {
			return (float[][]) methods[CellSymetries.getIndex(n, c)].invoke(null,
					new Object[] { new Float(x), new Float(y), new Float(z) });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		float[][] m = pos(1, 0, 1.5f, 2.3f, 4.6f);
		for (int i = 0; i < m.length; i++)
			System.out.println(m[i][0] + "," + m[i][1] + "," + m[i][2]);
	}
}
