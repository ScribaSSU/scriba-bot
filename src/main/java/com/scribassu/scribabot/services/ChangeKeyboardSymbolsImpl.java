package com.scribassu.scribabot.services;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;

@Service
public class ChangeKeyboardSymbolsImpl implements ChangeKeyboardSymbols{
    private HashMap<Character, String> symbols = new HashMap<>();

    public ChangeKeyboardSymbolsImpl()
    {
        symbols.put('\"', "%22");
        symbols.put('{', "%7b");
        symbols.put('}', "%7d");
        symbols.put('[', "%5b");
        symbols.put(']', "%5d");
        symbols.put('\\', "%5c");
        symbols.put(' ', "%20");
        symbols.put(':', "%3a");
        symbols.put('А', "%d0%90");
        symbols.put('Б', "%d0%91");
        symbols.put('В', "%d0%92");
        symbols.put('Г', "%d0%93");
        symbols.put('Д', "%d0%94");
        symbols.put('Е', "%d0%95");
        symbols.put('Ж', "%d0%96");
        symbols.put('З', "%d0%97");
        symbols.put('И', "%d0%98");
        symbols.put('Й', "%d0%99");
        symbols.put('К', "%d0%9a");
        symbols.put('Л', "%d0%9b");
        symbols.put('М', "%d0%9c");
        symbols.put('Н', "%d0%9d");
        symbols.put('О', "%d0%9e");
        symbols.put('П', "%d0%9f");
        symbols.put('Р', "%d1%a0");
        symbols.put('С', "%d1%a1");
        symbols.put('Т', "%d1%a2");
        symbols.put('У', "%d1%a3");
        symbols.put('Ф', "%d1%a4");
        symbols.put('Х', "%d1%a5");
        symbols.put('Ц', "%d1%a6");
        symbols.put('Ч', "%d1%a7");
        symbols.put('Ш', "%d1%a8");
        symbols.put('Щ', "%d1%a9");
        symbols.put('Ъ', "%d1%aa");
        symbols.put('Ы', "%d1%ab");
        symbols.put('Ь', "%d1%ac");
        symbols.put('Э', "%d1%ad");
        symbols.put('Ю', "%d1%ae");
        symbols.put('Я', "%d1%af");
        symbols.put('а', "%d0%b0");
        symbols.put('б', "%d0%b1");
        symbols.put('в', "%d0%b2");
        symbols.put('г', "%d0%b3");
        symbols.put('д', "%d0%b4");
        symbols.put('е', "%d0%b5");
        symbols.put('ж', "%d0%b6");
        symbols.put('з', "%d0%b7");
        symbols.put('и', "%d0%b8");
        symbols.put('й', "%d0%b9");
        symbols.put('к', "%d0%ba");
        symbols.put('л', "%d0%bb");
        symbols.put('м', "%d0%bc");
        symbols.put('н', "%d0%bd");
        symbols.put('о', "%d0%be");
        symbols.put('п', "%d0%bf");
        symbols.put('р', "%d1%80");
        symbols.put('с', "%d1%81");
        symbols.put('т', "%d1%82");
        symbols.put('у', "%d1%83");
        symbols.put('ф', "%d1%84");
        symbols.put('х', "%d1%85");
        symbols.put('ц', "%d1%86");
        symbols.put('ч', "%d1%87");
        symbols.put('ш', "%d1%88");
        symbols.put('щ', "%d1%89");
        symbols.put('ъ', "%d1%8a");
        symbols.put('ы', "%d1%8b");
        symbols.put('ь', "%d1%8c");
        symbols.put('э', "%d1%8d");
        symbols.put('ю', "%d1%8e");
        symbols.put('я', "%d1%8f");
    }

    @Override
    public void changeSymbols (String fileFrom, String fileTo)
    {
        try {
            KeyboardFileReader reader = new KeyboardFileReaderImpl();
            String line = reader.readAsString(fileFrom);
            StringBuilder res = new StringBuilder();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < line.length(); i++)
            {
                char symbol = line.charAt(i);
                if (symbols.containsKey(symbol))
                {
                    stringBuilder.append(symbols.get(symbol));
                }
                else
                {
                    stringBuilder.append(symbol);
                }
            }
            res.append(stringBuilder.toString());
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileTo));
            bufferedWriter.write(res.toString());
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

