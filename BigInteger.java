import java.util.ArrayList;

public class BigInteger {
    private DigitList digits;
    private int sign;

    public BigInteger() {
        this.digits = null;
        this.sign = 1;
    }

    public BigInteger(DigitList L) {
        this.digits = L;
        this.sign = 1;
    }

    public BigInteger(int i, DigitList L) {
        this.digits = L;
        this.sign = sgn(i);
    }

    public BigInteger(int i) {
        this.digits = DigitList.digitize(Math.abs(i));
        this.sign = sgn(i);
    }

    public BigInteger(String str) {
        if (str.charAt(0) == '-') {
            str = str.substring(1);
            this.digits = DigitList.digitize(str);
            this.sign = -1;
        } else {
            this.digits = DigitList.digitize(str);
            this.sign = 1;
        }
    }

    public DigitList getDigits() {
        return this.digits;
    }

    public int getSign() {
        return this.sign;
    }

    public void setDigits(DigitList digits) {
        this.digits = digits;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    private int sgn(int i) {
        if (i < 0)
            return -1;
        else
            return 1;
    }

    public int length() {
        if (this.digits == null)
            return 0;
        else
            return this.digits.length();
    }

    public BigInteger copy() {
        if (this.digits == null)
            return new BigInteger(0);
        else
            return new BigInteger(this.sign, this.digits.copy());
    }

    public BigInteger trimDigit() {
        return new BigInteger(this.sign, DigitList.trimDigitList(this.digits));
    }

    public boolean equals(Object obj) {
        if (obj instanceof BigInteger) {
            BigInteger other = (BigInteger) obj;
            if (this.sign == other.sign && DigitList.compareDigitLists(this.digits, other.digits) == 0) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        if (this.digits != null) {
            String integer = "";
            DigitList tmp = this.digits;
            integer = tmp.getDigit() + integer;
            tmp = tmp.getNextDigit();
            while (tmp != null) {
                integer = tmp.getDigit() + integer;
                tmp = tmp.getNextDigit();
            }
            return ((this.sign == -1) ? "-" : "") + integer;
        } else
            return "";
    }

    public BigInteger leftDigits(int n) {
        return new BigInteger(this.digits.leftDigits(n));
    }

    public BigInteger rightDigits(int n) {
        return new BigInteger(this.digits.rightDigits(n));
    }

    public BigInteger shift(int n) {
        if (n < 1)
            return this;
        else {
            BigInteger clone = this.copy();
            clone.digits = new DigitList(0, clone.digits);
            return clone.shift(n - 1);
        }
    }

    /********************************
     * STUDENT'S CODE
     ********************************/

    public BigInteger add(BigInteger other) {
        // complete this code
        if (this.sign == other.sign) // a, b cung dau
            return new BigInteger(this.sign, DigitList.addDigitLists(0, this.digits, other.digits));
        else {
            if (this.sign == -1) {// a < 0
                if (DigitList.compareDigitLists(this.digits, other.digits) == 1) { // mang dau duong (>0)
                    return new BigInteger(-1, DigitList.subDigitLists(0, this.digits, other.digits));
                } else {
                    return new BigInteger(1, DigitList.subDigitLists(0, other.digits, this.digits));
                }
            } else {
                if (DigitList.compareDigitLists(this.digits, other.digits) >= 0) {
                    return new BigInteger(1, DigitList.subDigitLists(0, this.digits, other.digits));
                } else {
                    return new BigInteger(-1, DigitList.subDigitLists(0, other.digits, this.digits));
                }
            }
        }
    }

    public BigInteger sub(BigInteger other) {
        // code here
        DigitList temp = new DigitList();
        int sgn = 0;
        if (other.sign == -1 && this.sign == 1) {
            sgn = 1;
            temp = DigitList.addDigitLists(0, this.digits, other.digits);
        } else if (other.sign == 1 && this.sign == -1) {
            sgn = -1;
            temp = DigitList.addDigitLists(0, this.digits, other.digits);
        } else if (other.sign == -1 && this.sign == -1) {
            if (DigitList.compareDigitLists(this.digits, other.digits) == 1) {
                sgn = -1;
                temp = DigitList.subDigitLists(0, this.digits, other.digits);
            } else {
                sgn = 1;
                temp = DigitList.subDigitLists(0, other.digits, this.digits);
            }
        } else if (other.sign == 1 && this.sign == 1) {
            if (DigitList.compareDigitLists(this.digits, other.digits) >= 0) {
                sgn = 1;
                temp = DigitList.subDigitLists(0, this.digits, other.digits);
            } else {
                sgn = -1;
                temp = DigitList.subDigitLists(0, other.digits, this.digits);
            }
        }

        // return (DigitList.compareDigitLists(temp, new DigitList()) == 0) ? new
        // BigInteger(sgn, temp)
        // : new BigInteger(1, temp);
        return new BigInteger(sgn, temp);
    }

    public BigInteger mul(BigInteger other) {
        if (this.digits.length() == 1 && other.digits.length() == 1) {
            int tmp11 = (this.sign == other.sign) ? 1 : -1;
            return new BigInteger(tmp11, new DigitList(this.digits.getDigit() * other.digits.getDigit(), null));
        }
        if (this.digits.length() < other.digits.length()) {
            int tmp = other.digits.length() - this.digits.length();
            DigitList temp = this.digits;
            while (tmp > 0) {
                if (temp.getNextDigit() != null) {
                    temp = temp.getNextDigit();
                } else {
                    temp.setNextDigit(new DigitList());
                    temp = temp.getNextDigit();
                    tmp--;
                }
            }
        } else {
            int tmp = this.digits.length() - other.digits.length();
            DigitList temp = other.digits;
            while (tmp > 0) {
                if (temp.getNextDigit() != null) {
                    temp = temp.getNextDigit();
                } else {
                    temp.setNextDigit(new DigitList());
                    temp = temp.getNextDigit();
                    tmp--;
                }
            }
        }
        int l = this.digits.length() / 2;
        int r = this.digits.length() - l;
        if (this.sign != other.sign)
            return new BigInteger(-1,
                    DigitList.trimDigitList(this.leftDigits(l).mul(other.leftDigits(l))
                            .add(this.rightDigits(r).mul(other.leftDigits(l))
                                    .add(this.leftDigits(l).mul(other.rightDigits(r))).shift(l))
                            .add(this.rightDigits(r).mul(other.rightDigits(r)).shift(2 * l)).getDigits()));
        else
            return new BigInteger(1,
                    DigitList.trimDigitList(this.leftDigits(l).mul(other.leftDigits(l))
                            .add(this.rightDigits(r).mul(other.leftDigits(l))
                                    .add(this.leftDigits(l).mul(other.rightDigits(r))).shift(l))
                            .add(this.rightDigits(r).mul(other.rightDigits(r)).shift(2 * l)).getDigits()));
    }

    public static BigInteger pow(BigInteger X, BigInteger Y) {
        int sign = X.getSign();
        X.setSign(1);
        DigitList temp = new DigitList(1, null);
        BigInteger value = new BigInteger(1);
        while (DigitList.compareDigitLists(Y.getDigits(), temp) >= 0) {
            value = value.mul(X);
            Y = Y.sub(new BigInteger(1));

        }
        value.setSign(sign);
        return value;
    }

    public static BigInteger factorial(BigInteger X) {
        // code here
        BigInteger temp = new BigInteger(DigitList.digitize("1"));
        BigInteger value = new BigInteger(DigitList.digitize("1"));
        while (DigitList.compareDigitLists(X.getDigits(), temp.getDigits()) >= 0) {
            value = value.mul(X);
            X = X.sub(new BigInteger(1));
        }
        return value;
    }

    public static BigInteger computeValue(ArrayList<BigInteger> operandArr, ArrayList<Character> operatorArr) {
        // complete - and * operator
        BigInteger output = operandArr.get(0);
        for (int j = 0; j < operatorArr.size(); j++) {
            switch (operatorArr.get(j)) {
                case '+':
                    output = output.add(operandArr.get(j + 1));
                    break;
                case '-':
                    output = output.sub(operandArr.get(j + 1));
                    break;
                case '*':
                    output = output.mul(operandArr.get(j + 1));
                    break;
                default:
                    break;
            }
        }
        return output;
    }
}