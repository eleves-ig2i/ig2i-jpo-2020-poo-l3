package graphics;

import io.InstanceReader;
import modele.Instance;
import modele.Shift;
import modele.Solution;
import modele.Tournee;
import vue.VueSolution;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SolutionVisualization {

    private final static int HEIGHT_RECT = 40;
    private final static int HEIGHT_ECHELLE = 50;
    private final static int SIZE_BETWEEN_SHIFT = 15;
    private final static int MINUTE_TO_PIXEL = 2;
    private final static int PADDING_X = 15;
    private final static int HEIGHT_POINTILLE = 7;
    private final static int T_MARGIN_RIGHT = 9;
    private static final int T_MARGIN_BOTTOM = 3;
    private static final int SIZE_FLECHE_BOUT = 30;

    private int width, height;
    private Graphics2D g2d;
    private BufferedImage bi;
    private Solution solution;
    private int first_minute_x;
    private int index;
    int temps_min_instance;
    int temps_max_instance;


    public SolutionVisualization(Solution s) {
        solution = s;

        first_minute_x = solution.getTempsMinDebutInstance();
        int nb_shift = s.getShiftList().size();
        height = Math.max(nb_shift*(HEIGHT_RECT+SIZE_BETWEEN_SHIFT) + HEIGHT_ECHELLE,860);
        temps_min_instance = s.getTempsMinDebutInstance();
        temps_max_instance = s.getTempsMaxFinInstance();
        width = Math.max((((temps_max_instance-temps_min_instance)*MINUTE_TO_PIXEL + 50) + SIZE_FLECHE_BOUT + 10),1550);
        bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = bi.createGraphics();
        //g2d.setColor(Color.WHITE);
        Color startColor = new Color(58,90,94);
        Color endColor = new Color(15,22,35);

        int startX = 0, startY = 0, endX = 0, endY = height;

        GradientPaint gradient = new GradientPaint(startX, startY, startColor, endX, endY, endColor);
        g2d.setPaint(gradient);
        g2d.fillRect(0,0,width,height);
        drawShifts();
        traceLechelle(temps_min_instance, temps_max_instance);
    }

    /**
     * Un temps mort est représentée par une tournée car il a un temps de début et de fin aussi
     * dans cette fonction on parcourt tout les temps mort et on appelle la fonction la fonction draw
     */
    private void drawTempsMorts(int y, Shift s) {
        ArrayList<Tournee> tempsMortList = s.getListeTempsMort();
        for (Tournee t:tempsMortList) {
            if (t.getDateFin().getMinutesCorrespondantes()>temps_max_instance)
                drawTempsMort((s.getTempsDebut()-(t.getDuree())-first_minute_x)*MINUTE_TO_PIXEL,y,t.getDuree());
            else
                drawTempsMort((t.getDateDebut().getMinutesCorrespondantes()-first_minute_x)*MINUTE_TO_PIXEL, y, t.getDuree());
        }
    }

    /**
     * Draw un rectangle rouge qui représente le temps mort caractérisé par les infos passés en paramètres
     */
    private void drawTempsMort(int x, int y, int temps){
        g2d.setColor(Color.RED);
        g2d.fillRect(PADDING_X + x, y, MINUTE_TO_PIXEL *temps, HEIGHT_RECT);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(PADDING_X + x, y, MINUTE_TO_PIXEL *temps, HEIGHT_RECT);
        g2d.setStroke(new BasicStroke(1));
    }

    /**
     * Dessine l'échelle en bas de l'image
     */
    private void traceLechelle(int tempsMin, int tempsMax) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, height-35, PADDING_X + width-20, 2);
        g2d.fillPolygon(new int[]{PADDING_X+width-15,PADDING_X+width-25,PADDING_X+width-25},new int[]{height-35,height-43, height-27},3);

        int heure_min,heure_max;

        if (tempsMin%60==0)
            heure_min = tempsMin/60;
        else
            heure_min = tempsMin/60+1;

        heure_max = tempsMax/60+1;

        for(int i=0;i<(heure_max-heure_min);i++){
            g2d.setColor(new Color(200,200,200));
            int c = 0;
            while (c<height-35) {
                g2d.drawLine(PADDING_X + 60 * i * MINUTE_TO_PIXEL, c, PADDING_X + 60 * MINUTE_TO_PIXEL * i, c+HEIGHT_POINTILLE);
                c+=HEIGHT_POINTILLE*2;
            }
            Font font = new Font("TimesRoman", Font.PLAIN, 18);
            g2d.setFont(font);
            String minutes = heure_min+i+"h";
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int stringWidth = fontMetrics.stringWidth(minutes);
            g2d.setPaint(Color.WHITE);
            g2d.drawString(minutes, PADDING_X + 60*i*MINUTE_TO_PIXEL - (stringWidth) / 2, height-7);
        }

        for(int i=0;i<(heure_max-heure_min)*3-2;i++){
            g2d.setPaint(Color.WHITE);
            g2d.drawLine(PADDING_X + 20 * i * MINUTE_TO_PIXEL, height-35, PADDING_X + 20 * MINUTE_TO_PIXEL * i, height-25);
        }

    }

    private Color getRandomColor(){
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        while (r<0.6)
            r = rand.nextFloat();
        while (b<0.6)
            b = rand.nextFloat();
        while (g<0.6)
            g = rand.nextFloat();


        return new Color(r, g, b);
    }

    /**
     * Dessine un rectangle représentant une tournée et écris son nom dedans
     */
    private void drawTournee(int x, int y, int temps){
        g2d.setColor(getRandomColor());
        g2d.fillRect(PADDING_X + x, y, MINUTE_TO_PIXEL *temps, HEIGHT_RECT);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(PADDING_X + x, y, MINUTE_TO_PIXEL *temps, HEIGHT_RECT);
        g2d.setStroke(new BasicStroke(1));
        ecrisNomTournee(index, x, y,  MINUTE_TO_PIXEL *temps);
        index++;
    }

    /**
     * Cette fonction prend un shift en paramètre et appelle les fonctions nécessaires pour dessiner le shift
     * @param s
     * @param y
     */
    private void drawShift(Shift s, int y){
        for (Tournee t:s.getTourneeList()) {
            drawTournee((t.getDateDebut().getMinutesCorrespondantes()-first_minute_x)*MINUTE_TO_PIXEL,y,t.getDateFin().getMinutesCorrespondantes()-t.getDateDebut().getMinutesCorrespondantes());
        }
    }

    /**
     * dessine tous les shifts
     */
    private void drawShifts(){
        int actual_y = 0;
        index = 1;
        for (Shift s:solution.getShiftList()) {
            drawTempsMorts(actual_y, s);
            drawShift(s,actual_y);
            actual_y+=HEIGHT_RECT+SIZE_BETWEEN_SHIFT;
        }
    }

    /**
     * met un texte au milieu du rectangle de la tournée
     */
    private void ecrisNomTournee(int index, int x, int y, int width_tournee){
        String index_str = String.valueOf(index);
        Font font = new Font("Aria", Font.PLAIN, 18);
        Font font2 = new Font("Aria", Font.PLAIN, 13);

        g2d.setPaint(Color.black);

        FontMetrics fontMetrics = g2d.getFontMetrics();
        int stringWidth = fontMetrics.stringWidth("t");
        int stringHeight = fontMetrics.getAscent();

        FontMetrics fontMetrics2 = g2d.getFontMetrics();
        int stringWidth2 = fontMetrics2.stringWidth(index_str);
        int stringHeight2 = fontMetrics2.getAscent();

        g2d.setFont(font);
        g2d.drawString("t", PADDING_X + x + (width_tournee - (stringWidth+stringWidth2)) / 2, y + HEIGHT_RECT / 2 + (stringHeight+stringHeight2) / 4);
        g2d.setFont(font2);
        g2d.drawString(index_str, PADDING_X + x + (width_tournee + T_MARGIN_RIGHT - (stringWidth2)) / 2, y + HEIGHT_RECT / 2 + T_MARGIN_BOTTOM + (stringHeight+stringHeight2) / 4);
    }

    /**
     * Enregistre une image
     * @param version
     * @return
     * @throws IOException
     */
    public String saveImage(int version) throws IOException {
        String path = "solution_ressources_draws/" + solution.getInstanceName();
        new File(path).mkdir();
        String imageName = "solution_";
        switch (version) {
            case 0 : //v0 triviale
                imageName += "v0.png";
                break;
            case 1 :
                imageName += "v1.png";
                break;
            case 2 :
                imageName += "v2.png";
                break;
            case 3 :
                imageName += "v3.png";
                break;
        }
        String str = path + "/" + imageName;
        ImageIO.write(bi, "PNG", new File(str));
        return str;
    }

    public static void main(String[] args) throws Exception {
        InstanceReader reader = new InstanceReader("instances/instance_10.csv");
        Instance c = reader.readInstance();
        Solution e = new Solution(c);
        e.findSolutionInter();
        SolutionVisualization s = new SolutionVisualization(e);
        s.saveImage(0);
    }

}