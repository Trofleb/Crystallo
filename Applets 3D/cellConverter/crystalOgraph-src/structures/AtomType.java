/*
 * @(#)AtomType.java	1.0 96/03/18
 */

package structures;

import java.awt.Color;

/**
 * AtomData class (accomodates for data related to atom types)
 * A zero value of any of its fields means this field is not present
 * for the specific atom type.
 * Data are take from
 * <a href="http://www2.shef.ac.uk/chemistry/web-elements">http://www2.shef.ac.uk/chemistry/web-elements</a>.
 */
class AtomData {
  /**
   * The atomic number.
   */
  public int number;

  /**
   * The atomic weight.
   */
  public double weight;

  /**
   * The symbol.
   */
  public String symbol;

  /**
   * The name of the element in the English spelling.
   */
  public String name;

  /**
   * The atomic radius (in Angstrom.).
   */
  public double atRadius;

  /**
   * The covalent radius (in Angstrom.).
   */
  public double covRadius;

  /**
   * The van der Waals radius (in Angstrom.).
   */
  public double vdWradius;

  /**
   * The radius metallic(12) (in Angstrom.).
   */
  public double metRadius;

  /**
   * A color index for visualization.
   * @see Atom_color
   */
  public int color_idx;

  /**
   * Constructor defining all data fields. 0.0 is used for unknown data fields.
   */
  public AtomData(int nmbr, double wght, String smbl, String nm, double atRds,
                  double covRds, double vdWrds, double metRds, int clr_idx) {
    number = nmbr;
    weight = wght;
    symbol = smbl;
    name = nm;
    atRadius = atRds;
    covRadius = covRds;
    vdWradius = vdWrds;
    metRadius = metRds;
    color_idx = clr_idx;
  }
}

/**
 * Atom_color class     (rgb color representation for atoms)
 */
class Atom_color {
  public int red;
  public int green;
  public int blue;

  /**
   * Constructor, individual color components should be between 0 and 255
   */
  public Atom_color(int r, int g, int b) {
    red = r;
    green = g;
    blue = b;
  }

  public float fred() {
    return (float) (red / 255.0);
  }

  public float fgreen() {
    return (float) (green / 255.0);
  }

  public float fblue() {
    return (float) (blue / 255.0);
  }
}



/**
 * AtomType class	(representation for atom types)
 * Uses a static array with data for each element.
 * @see AtomData
 */
public class AtomType {
  /**
   * A reference to an entry in the static AtomData array.
   */ 
  protected AtomData impl;  

  /* instance creation */

  /**
   * Constructor for a dummy atom type, to be initialized.
   */
  public AtomType() {
    impl = data[0];
  }

  /**
   * Constructor for an atom type denoted by its atom symbol.
   */
  public AtomType(String symbol) {
    int number = data.length;

    while (--number > 0)
      if (symbol.equals(data[number].symbol))
        break;
    impl = data[number];
  }

  /**
   * Constructor for an atom type denoted by its atomic number.
   */
  public AtomType(int number) {
    if (number < 0 || number > data.length)
      impl = data[0];
    else
      impl = data[number];
  }

  /* accessing */

  /**
   * Return the atomic number.
   */
  public int atomicNumber() {
    return impl.number;
  }

  /**
   * Return the atomic radius.
   * 0.0 is returned in the case it is not specified.
   */
  public double atomicRadius() {
    return impl.atRadius;
  }

  /**
   * Return the atomic weight.
   */
  public double atomicWeight() {
    return impl.weight;
  }

  /**
   * Return the atom symbol.
   */
  public String atomSymbol() {
    return impl.symbol;
  }

  /**
   * Return the covalent radius.
   * 0.0 is returned in the case it is not specified.
   */
  public double covalentRadius() {
    return impl.covRadius;
  }

  /**
   * Return the full name (default in English spelling).
   */
  public String fullName() {
    return impl.name;
  }

  /**
   * Return the radius metallic(12).
   * 0.0 is returned in the case it is not specified.
   */
  public double metallicRadius() {
    return impl.metRadius;
  }

  /**
   *
   * Return the van der Waals radius.
   * 0.0 is returned in the case it is not specified.
   */
  public double vanderWaalsRadius() {
    return impl.vdWradius;
  }

  /**
   * Return the standard color of this atom type.
   */
  public Color color() {
    return atom_color_tab[impl.color_idx];
  }


  /* comparing */

  /**
   * Return a integer unique to the receiver.
   */
  public int hashCode() {
    return atomicNumber();
  }

  /**
   * Return true if the receiver is equal to at.
   */
  public boolean equals(Object o) {
    try {
      AtomType at = (AtomType) o;
      return impl.number == at.impl.number;
    } catch (Exception e) {
      return false;
    }
  }

  /* printing */

  /**
   * Return a text representation of the receiver.
   */
  public synchronized String toString() {
    return new String("ATOM(" + impl.name + ')');
  }

  /**
   * A static array containing the actual atom data.
   * Data are obtained from  http://www2.shef.ac.uk/chemistry/web-elements
   */
  protected static final AtomData data[] = {
    new AtomData(  0, 0.0,        "NONE", "NONEXISTENT",    0.0,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  1, 1.00794,    "H",    "hydrogen",      0.373,    0.300,  1.200,     0.0,  4  ),
    new AtomData(  1, 2.0013,     "D",    "deuterium",     0.373,    0.300,  1.200,     0.0,  4  ),
    new AtomData(  2, 4.0026,     "He",   "helium",        0.540,     0.0,   1.400,     0.0,  5  ),
    new AtomData(  3, 6.941,      "Li",   "lithium",       1.520,    1.230,    0.0,   1.220, 14  ),
    new AtomData(  4, 9.01218,    "Be",   "beryllium",     1.113,    0.890,    0.0,    0.890, 12  ),
    new AtomData(  5, 10.811,     "B",    "boron",         0.795,    0.810,    0.0,    0.800, 13  ),
    new AtomData(  6, 12.011,     "C",    "carbon",        0.772,    0.770,  1.700,     0.0,  0  ),
    new AtomData(  7, 14.0067,    "N",    "nitrogen",      0.549,    0.700,  1.550,     0.0,  1  ),
    new AtomData(  8, 15.9994,    "O",    "oxygen",        0.604,    0.660,  1.520,     0.0,  2  ),
    new AtomData(  9, 18.9984,    "F",    "fluorine",      0.709,    0.640,  1.470,     0.0,  6  ),
    new AtomData(  10, 20.1797,   "Ne",   "neon",          0.800,     0.0,  1.540,     0.0, 12  ),
    new AtomData(  11, 22.9898,   "Na",   "sodium",       1.858,   1.570,    0.0,   1.570,  7  ),
    new AtomData(  12, 24.305,    "Mg",   "magnesium",    1.599,   1.360,    0.0,   1.360, 15  ),
    new AtomData(  13, 26.9815,   "Al",   "aluminium",    1.432,   1.250,    0.0,   1.250,  9  ),
    new AtomData(  14, 28.0855,   "Si",   "silicon",      1.176,   1.170,  2.100,   1.170,  6  ),
    new AtomData(  15, 30.9738,   "P",    "phosphorus",   1.105,   1.100,  1.800,   1.100,  8  ),
    new AtomData(  16, 32.066,    "S",    "sulphur",      1.035,   1.040,  1.800,   1.040,  3  ),
    new AtomData(  17, 35.4527,   "Cl",   "chlorine",      0.994,    0.990,  1.750,     0.0, 13  ),
    new AtomData(  18, 39.948,    "Ar",   "argon",         0.960,     0.0,  1.880,     0.0, 12  ),
    new AtomData(  19, 39.0983,   "K",    "potassium",    2.272,   2.030,    0.0,   2.020, 12  ),
    new AtomData(  20, 40.078,    "Ca",   "calcium",      1.974,   1.740,    0.0,   1.740,  9  ),
    new AtomData(  21, 44.9559,   "Sc",   "scandium",     1.606,   1.440,    0.0,   1.440, 12  ),
    new AtomData(  22, 47.88,     "Ti",   "titanium",     1.448,   1.320,    0.0,   1.320,  9  ),
    new AtomData(  23, 50.9415,   "V",    "vanadium",     1.311,   1.220,    0.0,   1.220, 12  ),
    new AtomData(  24, 51.9961,   "Cr",   "chromium",     1.249,   1.180,    0.0,   1.190,  9  ),
    new AtomData(  25, 54.938,    "Mn",   "manganese",    1.367,   1.180,    0.0,   1.180,  9  ),
    new AtomData(  26, 55.847,    "Fe",   "iron",         1.241,   1.160,    0.0,   1.170,  8  ),
    new AtomData(  27, 58.9332,   "Co",   "cobalt",       1.253,   1.160,    0.0,   1.160, 12  ),
    new AtomData(  28, 58.6934,   "Ni",   "nickel",       1.246,   1.150,    0.0,   1.150, 10  ),
    new AtomData(  29, 63.546,    "Cu",   "copper",       1.278,   1.170,    0.0,   1.180, 10  ),
    new AtomData(  30, 65.39,     "Zn",   "zinc",         1.335,   1.250,    0.0,   1.210, 10  ),
    new AtomData(  31, 69.723,    "Ga",   "gallium",      1.221,   1.250,    0.0,   1.250, 12  ),
    new AtomData(  32, 72.61,     "Ge",   "germanium",    1.225,   1.220,    0.0,   1.240, 12  ),
    new AtomData(  33, 74.9216,   "As",   "arsenic",      1.245,   1.210,  1.850,   1.210, 12  ),
    new AtomData(  34, 78.96,     "Se",   "selenium",     1.160,   1.170,  1.900,   1.170, 12  ),
    new AtomData(  35, 79.904,    "Br",   "bromine",      1.145,   1.140,  1.850,     0.0, 10  ),
    new AtomData(  36, 0.838,      "Kr",   "krypton",       0.990,     0.0,  2.020,     0.0, 12  ),
    new AtomData(  37, 85.4678,   "Rb",   "rubidium",     2.475,   2.160,    0.0,   2.160, 12  ),
    new AtomData(  38, 87.62,     "Sr",   "strontium",    2.151,   1.910,    0.0,   1.910, 12  ),
    new AtomData(  39, 88.9058,   "Y",    "yttrium",      1.776,   1.620,    0.0,   1.620, 12  ),
    new AtomData(  40, 91.224,    "Zr",   "zirconium",    1.590,   1.450,    0.0,   1.450, 12  ),
    new AtomData(  41, 92.9064,   "Nb",   "niobium",      1.429,   1.340,    0.0,   1.340, 16  ),
    new AtomData(  42, 95.94,     "Mo",   "molybdenum",   1.363,   1.300,    0.0,   1.300, 12  ),
    new AtomData(  43, 97.9072,   "Tc",   "technetium",   1.352,   1.270,  2.060,   1.270, 12  ),
    new AtomData(  44, 101.07,    "Ru",   "ruthenium",    1.325,   1.250,    0.0,   1.250, 12  ),
    new AtomData(  45, 102.906,   "Rh",   "rhodium",      1.345,   1.250,    0.0,   1.250, 12  ),
    new AtomData(  46, 106.42,    "Pd",   "palladium",    1.376,   1.280,    0.0,   1.280, 12  ),
    new AtomData(  47, 107.868,   "Ag",   "silver",       1.445,   1.340,    0.0,   1.340,  9  ),
    new AtomData(  48, 112.411,   "Cd",   "cadmium",      1.489,   1.410,    0.0,   1.380, 12  ),
    new AtomData(  49, 114.818,   "In",   "indium",       1.626,   1.500,    0.0,   1.420, 12  ),
    new AtomData(  50, 118.71,    "Sn",   "tin",          1.405,   1.400,    0.0,   1.420, 12  ),
    new AtomData(  51, 121.757,   "Sb",   "antimony",     1.450,   1.410,  2.200,   1.390, 12  ),
    new AtomData(  52, 1.276,     "Te",   "tellurium",    1.432,   1.370,  2.200,   1.370, 12  ),
    new AtomData(  53, 126.904,   "I",    "iodine",       1.331,   1.330,  1.980,     0.0, 11  ),
    new AtomData(  54, 131.29,    "Xe",   "xenon",        1.090,     0.0,  2.160,     0.0, 12  ),
    new AtomData(  55, 132.905,   "Cs",   "caesium",      2.655,   2.530,    0.0,   2.350, 12  ),
    new AtomData(  56, 137.327,   "Ba",   "barium",       2.174,   1.980,    0.0,   1.980,  8  ),
    new AtomData(  57, 138.906,   "La",   "lanthanum",    1.870,   1.690,    0.0,   1.690, 12  ),
    new AtomData(  58, 140.115,   "Ce",   "cerium",       1.825,   1.650,    0.0,   1.650, 12  ),
    new AtomData(  59, 140.908,   "Pr",   "praseodymium", 1.820,   1.650,    0.0,   1.640, 12  ),
    new AtomData(  60, 144.24,    "Nd",   "neodymium",    1.814,   1.640,    0.0,   1.640, 12  ),
    new AtomData(  61, 144.913,   "Pm",   "promethium",     0.0,   1.630,    0.0,   1.630, 12  ),
    new AtomData(  62, 150.36,    "Sm",   "samarium",       0.0,   1.620,    0.0,   1.620, 12  ),
    new AtomData(  63, 151.965,   "Eu",   "europium",     1.995,   1.850,    0.0,   1.850, 12  ),
    new AtomData(  64, 157.25,    "Gd",   "gadolinium",   1.787,   1.610,    0.0,   1.620, 12  ),
    new AtomData(  65, 158.925,   "Tb",   "terbium",      1.763,   1.590,    0.0,   1.610, 12  ),
    new AtomData(  66, 1.625,     "Dy",   "dysprosium",   1.752,   1.590,    0.0,   1.600, 12  ),
    new AtomData(  67, 164.93,    "Ho",   "holmium",      1.743,   1.580,    0.0,   1.580, 12  ),
    new AtomData(  68, 167.26,    "Er",   "erbium",       1.734,   1.570,    0.0,   1.580, 12  ),
    new AtomData(  69, 168.934,   "Tm",   "thulium",      1.724,   1.560,    0.0,   1.580, 12  ),
    new AtomData(  70, 173.04,    "Yb",   "ytterbium",    1.940,   1.740,    0.0,   1.700, 12  ),
    new AtomData(  71, 174.967,   "Lu",   "lutetium",     1.718,   1.560,    0.0,   1.560, 12  ),
    new AtomData(  72, 178.49,    "Hf",   "hafnium",      1.564,   1.440,    0.0,   1.440, 12  ),
    new AtomData(  73, 180.948,   "Ta",   "tantalum",     1.430,   1.340,    0.0,   1.340, 12  ),
    new AtomData(  74, 183.84,    "W",    "tungsten",     1.370,   1.300,    0.0,   1.300, 12  ),
    new AtomData(  75, 186.207,   "Re",   "rhenium",      1.371,   1.280,    0.0,   1.280, 12  ),
    new AtomData(  76, 190.23,    "Os",   "osmium",       1.338,   1.260,    0.0,   1.260, 12  ),
    new AtomData(  77, 192.22,    "Ir",   "iridium",      1.357,   1.270,    0.0,   1.260, 12  ),
    new AtomData(  78, 195.08,    "Pt",   "platinum",     1.373,   1.300,    0.0,   1.290, 12  ),
    new AtomData(  79, 196.967,   "Au",   "gold",         1.442,   1.340,    0.0,   1.340,  6  ),
    new AtomData(  80, 200.59,    "Hg",   "mercury",      1.503,   1.440,    0.0,   1.390, 12  ),
    new AtomData(  81, 204.383,   "Tl",   "thallium",     1.700,   1.550,    0.0,   1.440, 12  ),
    new AtomData(  82, 2.072,     "Pb",   "lead",         1.750,   1.540,    0.0,   1.500, 12  ),
    new AtomData(  83, 208.98,    "Bi",   "bismuth",      1.545,   1.460,    0.0,   1.510, 12  ),
    new AtomData(  84, 208.982,   "Po",   "polonium",     1.673,   1.460,    0.0,     0.0, 12  ),
    new AtomData(  85, 209.987,   "At",   "astatine",       0.0,   1.450,    0.0,     0.0, 12  ),
    new AtomData(  86, 222.018,   "Rn",   "radon",          0.0,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  87, 223.02,    "Fr",   "francium",       0.0,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  88, 226.025,   "Ra",   "radium",         0.0,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  89, 227.028,   "Ac",   "actinium",     1.878,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  90, 232.038,   "Th",   "thorium",      1.798,   1.650,    0.0,   1.650, 12  ),
    new AtomData(  91, 231.036,   "Pa",   "protactinium", 1.561,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  92, 238.029,   "U",    "uranium",      1.385,   1.420,    0.0,   1.430, 12  ),
    new AtomData(  93, 237.048,   "Np",   "neptunium",    1.300,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  94, 244.064,   "Pu",   "plutonium",    1.513,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  95, 243.061,   "Am",   "americium",      0.0,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  96, 247.07,    "Cm",   "curium",         0.0,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  97, 247.07,    "Bk",   "berkelium",      0.0,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  98, 251.08,    "Cf",   "californium",    0.0,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  99, 252.083,   "Es",   "einsteinium",    0.0,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  100, 257.095,  "Fm",   "fermium",        0.0,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  101, 2.581,    "Md",   "mendelevium",    0.0,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  102, 259.101,  "No",   "nobelium",       0.0,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  103, 262.11,   "Lr",   "lawrencium",     0.0,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  104, 261.11,   "Rf",   "rutherfordium" , 0.0,     0.0,    0.0,     0.0, 12  ),
    new AtomData(  105, 262.114,  "Ha",   "hahnium",        0.0,     0.0,    0.0,     0.0, 12  )
  };

  /**
   * A color index table.
   * the RGB values in this table are referred to by the
   * color_idx field in the AtomData.
   */
  protected static final Color[] atom_color_tab = {
    new Color( 200, 200, 200 ),       /*  0 Light Grey     */
    new Color( 143, 143, 235 ),       /*  1 Sky Blue       */
    new Color( 240,   0,   0 ),       /*  2 Red            */
    new Color( 235, 200,  50 ),       /*  3 Yellow         */
    new Color( 235, 235, 235 ),       /*  4 White          */
    new Color( 235, 192, 203 ),       /*  5 Pink           */
    new Color( 218, 165,  32 ),       /*  6 Golden Rod     */
    new Color(   0,   0, 235 ),       /*  7 Blue           */
    new Color( 235, 165,   0 ),       /*  8 Orange         */
    new Color( 128, 128, 144 ),       /*  9 Dark Grey      */
    new Color( 165,  42,  42 ),       /* 10 Brown          */
    new Color( 160,  32, 240 ),       /* 11 Purple         */
    new Color( 235,  20, 147 ),       /* 12 Deep Pink      */
    new Color(   0, 235,   0 ),       /* 13 Green          */
    new Color( 178,  34,  34 ),       /* 14 Fire Brick     */
    new Color(  34, 139,  34 ),       /* 15 Forest Green   */
    new Color(   0, 206, 209 )        /* 16 Dark Turquoise */  
  };
}
