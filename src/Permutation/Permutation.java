import java.util.ArrayList;

/**
 * Permutationクラスは、置換の数学ライブラリです。
 * 副作用のない関数群だけで構成されています。
 * また、フィールド変数はすべて読み込み専用で、getterのみからアクセスできます。
 */
public class Permutation {

    /**
     * 置換の順序組のデータを保持します。
     * 配列のインデックスを元とし、配列の中身を像とします。
     * 読み込み専用で、値の取得には{@code getTuple()}を使用してください。
     */
    private int[] tuple;


    /**
     * 置換の次数を保持します。
     * 読み込み専用で、値の取得には{@code getSize(0}を使用してください。
     */
    private int size;


    /* ================================================================= */


    /**
     * 順序組からPermutationオブジェクトを生成します。
     *
     * @param origin 順序組のデータ
     * @throws IlligalPermutationException 要素数が2つ未満です
     * @throws IlligalPermutationException 順序組に重複があります
     * @throws IlligalPermutationException 対応付けられていない要素があります
     */
    public Permutation(int[] origin) throws IlligalPermutationException {
        if (origin.length < 2) {
            throw new IlligalPermutationException("not enough elements");
        }
        this.size = origin.length;

        for (int i = 0; i < origin.length; i++) {
            boolean found = false;
            for (int j = 0; j < origin.length; j++) {
                if (origin[j] != i) {
                    continue;
                }
                if (found) {
                    throw new IlligalPermutationException("Duplicate mapping found");
                }
                found = true;
            }
            if (!found) {
                throw new IlligalPermutationException("No mapped element found");
            }
        }
        this.tuple = new int[this.size];
        for (int i = 0; i < this.size; i++) {
            this.tuple[i] = origin[i];
        }
    }


    /**
     * 互換されたPermutationオブジェクトを返します。
     *
     * @param element1 互換する要素
     * @param element2 互換する要素
     * @return 互換されたPermutationオブジェクト
     * @throws IlligalPermutationException 指定された要素が見つかりません
     */
    public Permutation transposition(int element1, int element2) throws IlligalPermutationException {
        if (element1 < 0) {
            throw new IlligalPermutationException("out of bounds " + element1);
        }
        if (this.size <= element1) {
            throw new IlligalPermutationException("out of bounds " + element1);
        }
        if (element2 < 0) {
            throw new IlligalPermutationException("out of bounds " + element2);
        }
        if (this.size <= element2) {
            throw new IlligalPermutationException("out of bounds " + element2);
        }
        int[] transposed = new int[this.size];
        for (int i = 0; i < this.size; i++) {
            if (i == element1) {
                transposed[i] = this.tuple[element2];
            } else if (i == element2) {
                transposed[i] = this.tuple[element1];
            } else {
                transposed[i] = this.tuple[i];
            }
        }
        return new Permutation(transposed);
    }


    /**
     * 置換同士の積を返します。
     *
     * @param p 掛け合わせる置換
     * @return 置換の積
     */
    public Permutation product(Permutation p) {
        int pSize = p.getSize();
        int[] pTuple = p.getTuple();
        int[] producted;
        if (this.size == pSize) {
            producted = new int[this.size];
            for (int i = 0; i < this.size; i++) {
                producted[i] = this.tuple[pTuple[i]];
            }
        } else if (this.size > pSize) {
            producted = new int[this.size];
            for (int i = 0; i < this.size; i++) {
                if (i >= pSize) {
                    producted[i] = this.tuple[i];
                } else {
                    producted[i] = this.tuple[pTuple[i]];
                }
            }
        } else {
            producted = new int[pSize];
            for (int i = 0; i < pSize; i++) {
                if (i >= this.size) {
                    producted[i] = pTuple[i];
                } else {
                    producted[i] = pTuple[this.tuple[i]];
                }
            }
        }
        return new Permutation(producted);
    }


    /**
     * 置換同士の積を返します。
     *
     * @param p1 掛け合わせる置換
     * @param p2 掛け合わせる置換
     * @return 置換の積
     */
    public static Permutation product(Permutation p1, Permutation p2) {
        return p1.product(p2);
    }


    /**
     * 指定された次元数の恒等置換を返します。
     *
     * @param degree 次元数
     * @return 指定された次元数の恒等置換
     */
    public static Permutation identity(int degree) {
        int[] id = new int[degree];
        for (int i = 0; i < degree; i++) {
            id[i] = i;
        }
        return new Permutation(id);
    }


    /**
     * Shallow copyされたPermutationオブジェクトを返します。
     *
     * @return Shallow copyされたPermutationオブジェクト
     */
    public Permutation copy() {
        return new Permutation(this.tuple);
    }


    /**
     * 置換の順序組を返します。
     *
     * @return 順序組
     */
    public int[] getTuple() {
        return this.tuple;
    }


    /**
     * 置換の次元数を返します。
     *
     * @return 次元数
     */
    public int getSize() {
        return this.size;
    }


    /**
     * 要素に対応する像を返します。
     *
     * @param element 要素
     * @return 要素に対応する像
     * @throws IlligalPermutationException 指定された要素が見つかりません
     */
    public int getImage(int element) throws IlligalPermutationException {
        if (element < 0 || this.size <= element) {
            throw new IlligalPermutationException("out of bounds " + element);
        }
        return this.tuple[element];
    }


    /**
     * 逆置換を算出し、返します。
     *
     * @return 逆置換
     */
    public Permutation getInverse() {
        int[] inverse = new int[this.size];
        for (int i = 0; i < this.size; i++) {
            inverse[this.tuple[i]] = i;
        }
        return new Permutation(inverse);
    }


    /**
     * 指定された次元数の置換をランダムに生成し、返します。
     *
     * @param degree 次元数
     * @return ランダムな置換
     * @throws IlligalPermutationException 次元数に2未満は指定できません。
     */
    public static Permutation getRandomPermutation(int degree) throws IlligalPermutationException {
        int num = factorial(degree);
        ArrayList<Integer> parent = new ArrayList<Integer>();
        for (int i = 0; i < degree; i++) {
            parent.add(i);
        }
        ArrayList<Integer> list = new ArrayList<Integer>(parent);
        int[] temp = new int[degree];
        int[] arr = new int[degree];
        int n = (int)(Math.random() * num);
        for (int j = 0; j < degree; j++) {
            temp[degree - j - 1] = n % (j + 1);
            n /= (j + 1);
        }
        for (int j = 0; j < degree; j++) {
            arr[j] = list.get(temp[j]);
            list.remove(temp[j]);
        }
        return new Permutation(arr);
    }


    /**
     * 指定された次元数の置換群を算出し、返します。
     *
     * @param degree 次元数
     * @return 置換群
     * @throws IlligalPermutationException 次元数に2未満は指定できません。
     */
    public static Permutation[] getPermutationGroup(int degree) throws IlligalPermutationException {
        int num = factorial(degree);
        Permutation[] group = new Permutation[num];
        ArrayList<Integer> parent = new ArrayList<Integer>();
        for (int i = 0; i < degree; i++) {
            parent.add(i);
        }
        for (int i = 0; i < num; i++) {
            ArrayList<Integer> list = new ArrayList<Integer>(parent);
            int[] temp = new int[degree];
            int[] arr = new int[degree];
            int n = i;
            for (int j = 0; j < degree; j++) {
                temp[degree - j - 1] = n % (j + 1);
                n /= (j + 1);
            }
            for (int j = 0; j < degree; j++) {
                arr[j] = list.get(temp[j]);
                list.remove(temp[j]);
            }
            group[i] = new Permutation(arr);
        }
        return group;
    }


    /**
     * 指定された整数の階乗を再帰的に算出します。
     *
     * @param num 整数値
     * @return 求められた階乗の値
     */
    private static int factorial(int num) {
        if (num <= 0) {
            return 1;
        } else {
            return factorial(num - 1) * num;
        }
    }


    /**
     * このオブジェクトのコピーを作成して、返します。
     *
     * @return コピーされたオブジェクト
     */
    @Override
    protected Object clone() {
        return this.copy();
    }


    /**
     * このオブジェクトと他のオブジェクトが等しいかどうかを示します。
     *
     * @param obj 比較対象の参照オブジェクト
     * @return このオブジェクトが obj 引数と同じであるか否か
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Permutation)) {
            return false;
        }
        Permutation p = (Permutation) obj;
        if (this.size != p.getSize()) {
            return false;
        }
        int[] objTuple = ((Permutation) obj).getTuple();
        for (int i = 0; i < this.size; i++) {
            if (this.tuple[i] != objTuple[i]) {
                return false;
            }
        }
        return true;
    }


    /**
     * オブジェクトのハッシュコード値を返します。
     *
     * @return ハッシュコード値
     */
    public int hashCode() {
        return this.tuple.hashCode();
    }


    @Override
    public String toString() {
        String result = "";
        int digit = String.valueOf(this.size).length();
        String format = "%" + digit + "s";
        for (int i = 0; i < this.size; i++) {
            result += String.format(format, i);
            result += " -> ";
            result += String.format(format, this.tuple[i]);
            result += "\n";
        }
        return result;
    }
}


/**
 * Permutationクラス内で発生した例外のクラス。
 * 置換における数学的な例外が発生したときにthrowされます。
 */
class IlligalPermutationException extends RuntimeException {

    /**
     * 置換における数学的な例外に関する例外オブジェクトを生成します。
     *
     * @param str 例外についての説明文
     */
    public IlligalPermutationException(String str) {
        super("IlligalPermutationException: " + str);
    }
}
