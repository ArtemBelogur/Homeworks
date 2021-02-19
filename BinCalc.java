#include <iostream>
#include <cmath>

using namespace std;

const int type1 = 8;
const int type2 = 16;
const int type3 = 32;

class Number
{
public:
    int num;
    bool* binNum;
    int length;
    bool* multiply;

    Number(int num1)
    {
        num = num1;
        length = lengthOfBinNum();
        transformNum();
    }

    int lengthOfBinNum()
    {
        if (num <= 127 && num >= -127)
            return type1;
        else if (num <= 32767 && num >= -32767)
            return type2;
        else if (num <= 2147483647 && num >= -2147483647)
            return type3;
    }


    void transformNum()
    {
        binNum = new bool[length];
        int tmp = num;

        for (int i = length - 1; i >= 0; i--) {
            binNum[i] = (bool)(tmp % 2);
            tmp = tmp / 2;
        }

        if (num < 0) {
            invert(binNum);
            addAdditionalOne(binNum);
        }
    }

    void addAdditionalOne(bool* binNum1) {
        bool* additionalOne = new bool[length];
        additionalOne[length - 1] = 1;
        for (int i = 0; i < length - 1; i++)
            additionalOne[i] = 0;
        sum(binNum1, additionalOne, false);
    }

    void invert(bool* binNum1)
    {
        for (int i = length - 1; i >= 0; i--) {
            if (binNum1[i])
                binNum1[i] = 0;
            else
                binNum1[i] = 1;
        }
    }

    void sum(bool* binNum1, bool* binNum2, bool mult)
    {
        bool additional = false;

        for (int i = length; i >= 0; i--) {
            if (binNum1[i] == 1 && binNum2[i] == 1) {
                binNum1[i] = additional;
                additional = 1;
            }
            else if ((binNum1[i] == 0 && binNum2[i] == 1) || (binNum1[i] == 1 && binNum2[i] == 0)) {
                if (additional)
                    binNum1[i] = 0;
                else
                    binNum1[i] = 1;
            }
            else {
                binNum1[i] = additional;
                additional = 0;
            }
        }
    }

    void substraction(Number num)
    {
        invert(num.binNum);
        addAdditionalOne(num.binNum);
        sum(binNum, num.binNum, false);
    }

    void multiplication(Number num)
    {
        bool* tmp1 = new bool[length];
        bool* tmp2 = new bool[length];
        bool incorrect = false;

        for (int i = 0; i < length; i++) {
            tmp1[i] = binNum[i];
            tmp2[i] = num.binNum[i];
            binNum[i] = 0;
        }

        for (int i = length - 1, counter = 1; i >= 0; i--, counter++) {
            if (tmp2[i]) {
                for (int a = 0; a < length; a++) 
                    num.binNum[a] = 0;

                for (int j = length - 1, k = length - counter; k >= 0; j--, k--) 
                    num.binNum[k] = tmp1[j];

                for (int i = length - 1; i > counter; i--)
                    if (tmp1[i])
                        incorrect = true;

                sum(binNum, num.binNum, true);
            }
        }
    }

    void outputBinNum()
    {
        for (int i = 0; i < length; i++)
            cout << binNum[i];
    }

    void outputDecimalNum()
    {
        int num = 0;
        bool neg = false;

        if (binNum[0]) {
            invert(binNum);
            addAdditionalOne(binNum);
            neg = true;
        }

        for (int i = length - 1, j = 0; i >= 0; i--, j++)
            if (binNum[i] == 1) 
                num += int(pow(2, j));

        if (neg)
            cout << -num;
        else 
            cout << num;
    }
};

int isCorrect() {
    int check;
    do
    {
        cin >> check;
        if (cin.good())
            break;
        else
        {
            cout << "Invalid data!\n";
            cin.clear();
            while (cin.get() != '\n');
        }
    } while (1);
    return check;
}

int main()
{
    int firstNum, secondNum, operation;

    cout << "Enter the first number\n";
    firstNum = isCorrect();

    cout << "Enter the second number\n";
    secondNum = isCorrect();

    cout << "Enter the operation (1 - sum, 2 - multiplication, 3 - substraction)\n";
    operation = isCorrect();
    cout << endl;

    Number firstNumber = Number(firstNum);
    Number secondNumber = Number(secondNum);

    cout << "First number in a binary representation: ";
    firstNumber.outputBinNum();
    cout << endl << endl;

    cout << "Second number in a binary representation: ";
    secondNumber.outputBinNum();

    cout << endl << endl;

    if (firstNumber.lengthOfBinNum() != secondNumber.lengthOfBinNum()) {
        cout << "the binary code of these numbers are different!\n";
        return 0;
    }

    bool neg = false;
    bool pos = false;

    if (firstNumber.binNum[0] && secondNumber.binNum[0])
        neg = true;
    else if (!firstNumber.binNum[0] && !secondNumber.binNum[0])
        pos = true;


    try {
        if (operation == 1) {
            firstNumber.sum(firstNumber.binNum, secondNumber.binNum, false);
            cout << "Summary in a binary representation: ";
            firstNumber.outputBinNum();

            if (firstNumber.binNum[0] && pos) 
                throw 1;

            cout << endl << endl << "Summary in a decimal: ";
            firstNumber.outputDecimalNum();
        }
        else if (operation == 3) {
            firstNumber.substraction(secondNumber);
            cout << "Substraction in a binary representation: ";
            firstNumber.outputBinNum();

            if (firstNumber.binNum[0] && pos)
                throw 1;

            cout << endl << endl << "Substraction in a decimal: ";
            firstNumber.outputDecimalNum();
        }

        if (firstNumber.binNum[0] && pos) 
            throw 1;
    }
    catch(int e){
        if(e == 1)
            cout << endl << "Overflow! Output is incorrect\n";
    }

    /*switch (operation) {
    case 1:
        firstNumber.sum(firstNumber.binNum, secondNumber.binNum, false);
        cout << "Summary in a binary representation: ";
        firstNumber.outputBinNum();
        cout << endl << endl << "Summary in a decimal: ";
        firstNumber.outputDecimalNum();
        break;
    case 2:
        firstNumber.multiplication(secondNumber);
        cout << "Multyplication in a binary representation: ";
        firstNumber.outputBinNum();
        cout << endl << endl << "Multiplication in a decimal: ";
        firstNumber.outputDecimalNum();
        break;
    case 3:
        firstNumber.substraction(secondNumber);
        cout << "Substraction in a binary representation: ";
        firstNumber.outputBinNum();
        cout << endl << endl << "Substraction in a decimal: ";
        firstNumber.outputDecimalNum();
        break;
    }
    */
    return 0;
}
