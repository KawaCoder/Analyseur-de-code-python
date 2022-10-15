import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;


public class Main implements ActionListener
{

    JLabel l1, l2;
    JTextArea text;

    static JFrame f;

    Main(){
        f = new JFrame("ERREUR-DETECTOR-3000");
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(f, "Bon debuggage","Au revoir!", JOptionPane.INFORMATION_MESSAGE);
                e.getWindow().dispose();
            }
        });

        l1 = new JLabel();
        l1.setBounds(90,350,200,60);

        l2 = new JLabel();
        l2.setBounds(300,350,200,60);

        text = new JTextArea();
        text.setBounds(30,40,500,300);
        JScrollPane scroll = new JScrollPane (text, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(30,40,500,300);

        JButton btn = new JButton("Analyser");
        btn.setBounds(100,420,360,60);
        btn.addActionListener(this);


        JButton aboutBtn = new JButton("A propos");
        aboutBtn.setBounds(190,485,180,30);
        aboutBtn.addActionListener(this);




        //f.add(text);
        f.add(scroll);
        f.add(l1);
        f.add(l2);
        f.add(btn);
        f.add(aboutBtn);

        f.setSize(600,600);
        f.setLayout(null);
        f.setVisible(true);
    }



    int countWord(String str, String word){

        int count = 0, index = 0;
        while ((index = str.indexOf(word, index)) != -1 ){
            count++;
            index++;
        }
        return count;
    }

    ArrayList<Integer> getWordIndex(String str, String word){

        ArrayList<Integer> wordIndex = new ArrayList<Integer>();
        int index = str.indexOf(word);
        while(index >= 0) {
            wordIndex.add(index);
            index = str.indexOf(word, index+1);
        }
        return wordIndex;
    }

    public void actionPerformed(ActionEvent e) {
        if (Objects.equals(e.getActionCommand(), "Analyser")){
            String str = "";
            str = text.getText();
            //int ifIndex = 0;
            l1.setText("Lignes: "+ text.getLineCount());
            l2.setText("Characteres: "+ str.length());

            String[] strList = str.split("\\s+");



            //"=" au lieu de "=="
            int ifCount = countWord(str,"if")+countWord(str,"and")+countWord(str,"or")+countWord(str, "while");
            int operatorCount = countWord(str, "==")+countWord(str, ">=")+countWord(str, "<=")+countWord(str, "not");
            int singleEqual=Math.abs(ifCount-operatorCount);





            //Variables males ecrites
            ArrayList<String> variables = new ArrayList<>();
            int badvariables = 0;
            String variable = "";
            String[] lines = str.split("\\r?\\n");
            for (String line : lines) {
                if (line != null) {
                    char indent = ' ';
                    int indentLevel = 0;
                    while( indent == ' '){
                        indent=line.charAt(indentLevel);
                        indentLevel++;
                    }
                    try {
                        variable = line.substring((indentLevel-1), line.indexOf(" "));
                    }catch (StringIndexOutOfBoundsException ignored){}
                    if (!variable.startsWith("if") && !variable.startsWith("else") && !variable.startsWith("elif") && !variable.startsWith("while") && !variable.startsWith("def")) {
                        variables.add(variable);

                    }
                }
            }
            for (String s : variables) {
                for (String value : strList) {
                    if (JaroWinklerScore.compute(s, value) > 0.8 && JaroWinklerScore.compute(s, value) < 1.00) {
                        badvariables++;
                    }
                }
            }


            //Deux points manquants
            StringBuilder firstWords = new StringBuilder();
            for (String line : lines) {
                if (line != null) {
                    char indent = ' ';
                    int indentLevel = 0;
                    while (indent == ' ') {
                        indent = line.charAt(indentLevel);
                        indentLevel++;
                    }
                    try {
                        firstWords.append(line.substring((indentLevel - 1), line.indexOf(" ")));
                    } catch (StringIndexOutOfBoundsException ignored) {}
                }
            }
            int doubleDotKeyword = countWord(String.valueOf(firstWords), "if") + countWord(String.valueOf(firstWords), "else") + countWord(String.valueOf(firstWords), "while") + countWord(String.valueOf(firstWords), "for");
            int doubleDot = countWord(str, ":");
            int missingDoubleDot = Math.abs(doubleDotKeyword - doubleDot);




            //Erreurs de syntaxe
            int badSyntax = 0;
            String[] keyWords = {"if", "else", "while", "return", "and", "or", "for", "True", "False"};
            for (String keyWord : keyWords) {
                for (String value : strList) {
                    if (JaroWinklerScore.compute(keyWord, value) > 0.85 && JaroWinklerScore.compute(keyWord, value) < 1.00) {
                        badSyntax++;
                    }
                }
            }


            //Parenthese manquante
            int bracket = countWord(str,"(");
            int closedBracket = countWord(str, ")");
            int missingBracket = Math.abs(bracket - closedBracket);


            int total = (singleEqual+badvariables+badSyntax+missingBracket)*10;
            String message =
                    "-- \"=\" au lieu de \"==\"     :  "+singleEqual+
                            "\n-- Variables males reecrites :  "+badvariables+
                            "\n-- Mauvaise syntaxe:  "+badSyntax+
                            "\n-- Parenthese manquante:  "+missingBracket+
                            "\n-- \":\" manquant:  "+missingDoubleDot+
                            "\n\n         -Total: "+total+"€!-";

            JOptionPane.showMessageDialog(f,message,"Au revoir!", JOptionPane.ERROR_MESSAGE);



        }else if (Objects.equals(e.getActionCommand(), "A propos")){

            JOptionPane.showMessageDialog(f, "Ce programme est destine a la detection d'erreurs frequentes en python." +
                    " Il n'est pas parfait car il possede des limites causees par une elaboration simple et rapide destinee " +
                    "au divertissement. Bien que les algorithmes utilises sont elabores, le programme n'est pas pour autant " +
                    "infaillible, et peut mal fonctionner et presenter des \"bugs\".","A propos", JOptionPane.INFORMATION_MESSAGE);



        }




    }


    public static void main(String[] args) {

        FlatDarkLaf.setup();

        new Main();






    }
}