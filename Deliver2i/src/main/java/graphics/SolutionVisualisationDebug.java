package graphics;

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

public class SolutionVisualisationDebug {

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
    private int temps_min_instance;
    private int temps_max_instance;
    private String name;

    public SolutionVisualisationDebug(Solution s, String name) {
        solution = s;
        this.name = name;

        first_minute_x = solution.getTempsMinDebutInstance();
        int nb_shift = s.getShiftList().size();
        height = nb_shift*(HEIGHT_RECT+SIZE_BETWEEN_SHIFT) + HEIGHT_ECHELLE;
        temps_min_instance = s.getTempsMinDebutInstance();
        temps_max_instance = s.getTempsMaxFinInstance();
        width = ((temps_max_instance-temps_min_instance)*MINUTE_TO_PIXEL + 50) + SIZE_FLECHE_BOUT + 10;
        bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = bi.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0,0,width,height);
        drawShifts();
        traceLechelle(temps_min_instance, temps_max_instance);
    }

    /**
     * Un temps mort est représentée par une tournée car il a un temps de début et de fin aussi
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

    private void drawTempsMort(int x, int y, int temps){
        g2d.setColor(Color.RED);
        g2d.fillRect(PADDING_X + x, y, MINUTE_TO_PIXEL *temps, HEIGHT_RECT);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(PADDING_X + x, y, MINUTE_TO_PIXEL *temps, HEIGHT_RECT);
        g2d.setStroke(new BasicStroke(1));
    }

    private void traceLechelle(int tempsMin, int tempsMax) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, height-35, PADDING_X + width-20, 2);
        g2d.fillPolygon(new int[]{PADDING_X+width-15,PADDING_X+width-25,PADDING_X+width-25},new int[]{height-35,height-43, height-27},3);

        int heure_min,heure_max;

        if (tempsMin%60==0)
            heure_min = tempsMin/60;
        else
            heure_min = tempsMin/60+1;

        heure_max = tempsMax/60+1;

        for(int i=0;i<(heure_max-heure_min);i++){
            g2d.setColor(Color.GRAY);
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
            g2d.setPaint(Color.black);
            g2d.drawString(minutes, PADDING_X + 60*i*MINUTE_TO_PIXEL - (stringWidth) / 2, height-7);
        }

        for(int i=0;i<(heure_max-heure_min)*3-2;i++){
            g2d.setPaint(Color.black);
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

    private void drawShift(Shift s, int y){
        for (Tournee t:s.getTourneeList()) {
            drawTournee((t.getDateDebut().getMinutesCorrespondantes()-first_minute_x)*MINUTE_TO_PIXEL,y,t.getDateFin().getMinutesCorrespondantes()-t.getDateDebut().getMinutesCorrespondantes());
        }
    }

    private void drawShifts(){
        int actual_y = 0;
        index = 1;
        for (Shift s:solution.getShiftList()) {
            drawTempsMorts(actual_y, s);
            drawShift(s,actual_y);
            actual_y+=HEIGHT_RECT+SIZE_BETWEEN_SHIFT;
        }
    }

    public String saveImage() throws IOException {
        String str = "solution_ressources_draws/"+name+".png";
        ImageIO.write(bi, "PNG", new File(str));
        return str;
    }

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

    public static void visualisationSolution(Instance i) throws IOException {
        Solution s = null;
        if(i.getSolution().size() > 0)
        {
            s = i.getSolution().get(0);
        }

        if (i.getSolution() == null || i.getSolution().size() == 0) {
            System.out.println("Je dois recalculer la solution");
            s = new Solution(i);
            s.findSolutionInter();
        }
        else{
            System.out.println("Je ne dois pas recalculer la solution");
        }

        SolutionVisualization sv = new SolutionVisualization(s);
        String name_image = sv.saveImage(1);
        new VueSolution(name_image);
    }

}