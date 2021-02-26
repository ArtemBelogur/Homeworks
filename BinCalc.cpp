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
            Number ad = Number(1);

            substraction(ad);
            invert();
        }
    }

    void invert()
    {
        for (int i = length - 1; i >= 0; i--) {
            if (binNum[i])
                binNum[i] = 0;
            else
                binNum[i] = 1;
        }
    }

    void sum(Number num)
    {
        bool additional = false;

        bool neg = false;
        bool pos = false;

        if (binNum[0] && num.binNum[0])
            neg = true;

        if (!binNum[0] && !num.binNum[0])
            pos = true;

        for (int i = length; i >= 0; i--) {
            if (binNum[i] == 1 && num.binNum[i] == 1) {
                binNum[i] = additional;
                additional = 1;
            }
            else if ((binNum[i] == 0 && num.binNum[i] == 1) || (binNum[i] == 1 && num.binNum[i] == 0)) {
                if (additional)
                    binNum[i] = 0;
                else
                    binNum[i] = 1;
            }
            else {
                binNum[i] = additional;
                additional = 0;
            }
        }

        if ((binNum[0] && pos) || (!binNum[0] && neg))
            throw 1;
    }

    void substraction(Number num)
    {
        Number ad = Number(1);

        num.invert();
        num.sum(ad);
        sum(num);
    }

    void multiplication(Number num)
    {
        if (this->num < 0 && num.num < 0) {
            Number ad = Number(1);
            substraction(ad);
            invert();
            num.num = -(num.num);
            for (int i = 0; i < length; i++)
                num.binNum[i] = binNum[i];
        }
        else if (num.num < 0 && this->num > 0) {
            for (int i = 0; i < length; i++) 
                binNum[i] = num.binNum[i];
            int temp = num.num;
            num.num = this->num;
            this->num = temp;
        }
        else
            for (int i = 0; i < length; i++)
                num.binNum[i] = binNum[i];
        
        for (int i = 0; i < num.num - 1; i++) 
            sum(num);
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
        Number ad = Number(1);
        if (binNum[0]) {
            substraction(ad);
            invert();
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

    try {
        if (operation == 1) {
            firstNumber.sum(secondNumber);
            cout << "Summary in a binary representation: ";
            firstNumber.outputBinNum();
            cout << endl << endl << "Summary in a decimal: ";
            firstNumber.outputDecimalNum();
        }
        else if (operation == 2) {
            firstNumber.multiplication(secondNumber);
            cout << "Multyplication in a binary representation: ";
            firstNumber.outputBinNum();
            cout << endl << endl << "Multiplication in a decimal: ";
            firstNumber.outputDecimalNum();
        }
        else if (operation == 3) {
            firstNumber.substraction(secondNumber);
            cout << "Substraction in a binary representation: ";
            firstNumber.outputBinNum();
            cout << endl << endl << "Substraction in a decimal: ";
            firstNumber.outputDecimalNum();
        }
    }
    catch(int e){
        if(e == 1)
            cout << endl << "Overflow! Output is incorrect\n";
    }

    return 0;
}
