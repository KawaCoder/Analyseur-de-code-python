/*MIT License

Copyright (c) [year] [fullname]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

        text = new JTextArea("test = \"Bonjour\n" +
                "def fonction()\n" +
                "    if teSt = \"Bonjour\"\n" +
                "        print(\"Le code ci-dessus comporte des erreurs détéctables par ce logiciel.\")\n" +
                "        print(\" Cliquez sur \\\"Entrer\\\" pour analyser le code.\"\n" +
                "        Return Test");
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

        f.setSize(560,600);
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

    public void actionPerformed(ActionEvent e) {
        if (Objects.equals(e.getActionCommand(), "Analyser")){
            StringBuilder log = new StringBuilder();
            String str;
            str = text.getText();
            //int ifIndex = 0;
            l1.setText("Lignes: "+ text.getLineCount());
            l2.setText("Characteres: "+ str.length());

            String[] strList = str.split("\\s+");



            //"=" au lieu de "=="
            int ifCount = countWord(str,"if ")+countWord(str," and ")+countWord(str," or ")+countWord(str, "while ");
            int operatorCount = countWord(str, "==")+countWord(str, ">=")+countWord(str, "<=")+countWord(str, "not ");
            int singleEqual=Math.abs(ifCount-operatorCount);
            if (singleEqual==0){
                log.append("\n[ - ] Autant de \"==\" que de \"if, and, or, while.\"");


            }
            else{
                log.append("\n[ x ] ").append(operatorCount).append(" double signe egal/operateurs pour ").append(ifCount).append(" \"if, and, or, while\". Il en manque donc ").append(singleEqual);

            }




            //Variables males ecrites
            StringBuilder templog = new StringBuilder();
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
                        templog.append("\n[ x ] Ressemblance entre \"").append(s).append("\" et \"").append(value).append("\" a ").append(JaroWinklerScore.compute(s, value)).append(" pourcent");
                        badvariables++;
                    }
                }
            }
            log.append("\n__________________________________________\n[ - ] Variables detectees: ").append(variables).append(".").append(templog).append("\n__________________________________________\n");


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
                        if (!line.substring((indentLevel - 1)).contains(" ")){
                            firstWords.append(line.substring((indentLevel - 1)));
                        }else {
                            firstWords.append(line, (indentLevel - 1), line.indexOf(" ", indentLevel - 1));

                        }
                    } catch (StringIndexOutOfBoundsException ignored) {}
                }
            }
            int doubleDotKeyword = countWord(String.valueOf(firstWords), "if") + countWord(String.valueOf(firstWords), "else") + countWord(String.valueOf(firstWords), "while") + countWord(String.valueOf(firstWords), "for") + countWord(String.valueOf(firstWords), "def")+countWord(String.valueOf(firstWords), "elif");
            int doubleDot = countWord(str, ":");
            int missingDoubleDot = Math.abs(doubleDotKeyword - doubleDot);
            if (missingDoubleDot==0){
                log.append("\n[ v ] Autant de double-points que de \"if, else, while, for, def, elif.\"");


            }
            else{
                log.append("\n[ x ] ").append(operatorCount).append(" double-points pour ").append(doubleDotKeyword).append(" \"if, and, or, while\". Il en manque donc ").append(missingDoubleDot);

            }


            log.append("\n__________________________________________\n");


            //Erreurs de syntaxe
            int badSyntax = 0;
            String[] keyWords = {"if", "else", "while", "return", "and", "or", "for", "True", "False", "import"};
            for (String keyWord : keyWords) {
                for (String value : strList) {
                    //System.out.println(keyWord+" "+value+" "+JaroWinklerScore.compute(keyWord, value));

                    if (JaroWinklerScore.compute(keyWord, value) > 0.85 && JaroWinklerScore.compute(keyWord, value) < 1.00) {
                        badSyntax++;
                        log.append("\n[ x ] Ressemblance entre \"").append(value).append("\" et \"").append(keyWord).append("\" a ").append(JaroWinklerScore.compute(keyWord, value)).append(" pourcent");

                    }
                }
            }
            if (badSyntax==0){
                log.append("[ v ] La syntaxe est corecte.");

            }

            log.append("\n__________________________________________\n");


            //Parenthese manquante
            int bracket = countWord(str,"(");
            int closedBracket = countWord(str, ")");
            int missingBracket = Math.abs(bracket - closedBracket);
            if (missingBracket==0){
                log.append("\n[ - ] Autant de \" ( \" que de \")\"");


            }
            else{
                log.append("\n[ x ] ").append(bracket).append(" parenthese ouverte pour ").append(closedBracket).append(" parentheses fermees. Il en manque donc ").append(missingBracket);

            }


            log.append("\n__________________________________________\n");



            //Guillemet manquant
            int missingQuote = 0;
            int lineIndex = 0;
            for (String line : lines) {
                lineIndex++;
                if (countWord(line, "\"")%2 == 1){
                    missingQuote++;
                    log.append("\n[ x ] Nombre impair de guillemet detecte a la ligne d'indice ").append(lineIndex);

                }
            }




            int quote = countWord(str,"\"");


            int total = (singleEqual+badvariables+badSyntax+missingBracket+missingDoubleDot+missingQuote)*10;
            if (total==0){
                Object[] options1 = { "Ok", "Details"};
                JPanel msgPanel = new JPanel();
                msgPanel.add(new JLabel("Aucune erreur de débutant détéctée."));
                int result = JOptionPane.showOptionDialog(null, msgPanel, "Bravo!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options1, null);
                if (result == JOptionPane.NO_OPTION){
                    JPanel logPanel = new JPanel();
                    logPanel.add(new JTextArea(log.toString()));

                    JOptionPane.showMessageDialog(null, logPanel, "Log", JOptionPane.INFORMATION_MESSAGE);


                    //JOptionPane.showMessageDialog(f,("Details:\n"+log),"Details de l'analyse", JOptionPane.INFORMATION_MESSAGE);
                }

            }else {
                String message =
                        "-- \"=\" au lieu de \"==\"     :  " + singleEqual +
                                "\n-- Variables males reecrites :  " + badvariables +
                                "\n-- Mauvaise syntaxe:  " + badSyntax +
                                "\n-- Parenthese manquante:  " + missingBracket +
                                "\n-- Double-points manquant:  " + missingDoubleDot +
                                "\n-- Guillemets manquant:  " + missingQuote +
                                "\n\n         -Total: " + total + "€!-";
                //JOptionPane.showMessageDialog(f,message,"Erreurs détéctées", JOptionPane.ERROR_MESSAGE);

                Object[] options1 = { "Ok", "Details"};
                JPanel msgPanel = new JPanel();
                JTextArea errorArea = new JTextArea(message);
                errorArea.setEditable(false);
                msgPanel.add(errorArea);
                int result = JOptionPane.showOptionDialog(null, msgPanel, "Erreurs détéctées:", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options1, null);
                if (result == JOptionPane.NO_OPTION){
                    JPanel logPanel = new JPanel();
                    logPanel.add(new JTextArea(log.toString()));

                    JOptionPane.showMessageDialog(null, logPanel, "Log", JOptionPane.INFORMATION_MESSAGE);


                    //JOptionPane.showMessageDialog(f,("Details:\n"+log),"Details de l'analyse", JOptionPane.INFORMATION_MESSAGE);
                }
            }


//test code:
/*
test = "Bonjour
if teSt = "Bonjour"
    print("Le code ci-dessus comporte des erreurs détéctables par ce logiciel.")
    print(" Cliquez sur \"Entrer\" pour analyser le code."
            */



        }else if (Objects.equals(e.getActionCommand(), "A propos")){

            JOptionPane.showMessageDialog(f, "Ce programme est destine a la detection d'erreurs frequentes en python." +
                    " Il n'est pas parfait car il possede des limites causees par une elaboration simple et rapide destinee " +
                    "au divertissement. Bien que les algorithmes utilises sont elabores, le programme n'est pas pour autant " +
                    "infaillible, et peut mal fonctionner et presenter des \"bugs\". Programmation: https://github.com/DR34M-M4K3R","A propos", JOptionPane.INFORMATION_MESSAGE);



        }




    }


    public static void main(String[] args) {

        FlatDarkLaf.setup();

        new Main();






    }
}
