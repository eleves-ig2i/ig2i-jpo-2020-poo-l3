/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

        import exception.TourneeException;
        import graphics.SolutionVisualization;
        import io.InstanceReader;
        import io.exception.ReaderException;
        import modele.Instance;
        import modele.Solution;

        import java.awt.*;
        import java.awt.event.ItemEvent;
        import java.io.File;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.Comparator;
        import java.util.Date;
        import java.util.List;
        import javax.persistence.*;
        import javax.swing.*;

/**
 * @author hp
 */
public class Deliver2i extends JFrame {

    private ArrayList<String> instanceNomList;
    private Instance instanceVisu;
    private Solution solutionVisu;

    private static int varTh = 2;

    /**
     * Constructeur de la JFrame
     */
    public Deliver2i() {
        setUndecorated(true);
        initComponents();
        instanceNomList = getListNomInstances();
        initialisationFenetre();
    }

    /**
     * ajoute des nouvelles instructions pour init la frame, est appelé uniquement dans le constructeur
     */
    private void initialisationFenetre() {
        this.setTitle("Deliver2i");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        this.setBounds(0, 0, dim.width, winSize.height);
        remplirListeNomInstances();
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        labelNbShifts.setVisible(false);
        labelNbTM.setVisible(false);
        nbtempsmorts.setVisible(false);
        nbshift.setVisible(false);
        this.setVisible(true);
    }


    /**
     * Classe pour le fond dégradé du panel gauche
     */
    class jPanelGradient extends JPanel {
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();

            Color color1 = new Color(29, 45, 47);
            Color color2 = new Color(3, 4, 5);
            GradientPaint gp = new GradientPaint(0, 0, color1, 180, height, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }

    /**
     * Classe pour le fond dégradé du panel droite
     */
    class jPanelGradient2 extends JPanel {
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();

            Color color1 = new Color(58, 90, 94);
            Color color2 = new Color(6, 9, 10);
            GradientPaint gp = new GradientPaint(0, 0, color1, 180, height, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }

    /**
     * Classe pour le fond dégradé du jScrollPane
     */
    class jScrollGradient extends JScrollPane {
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();

            Color color1 = new Color(58, 90, 94);
            Color color2 = new Color(6, 9, 10);
            GradientPaint gp = new GradientPaint(0, 0, color1, 180, height, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }


    /**
     * recupere une instance en fonction de son nom
     * @param name
     * @return
     */
    public static Instance getInstancesByName(String name) {
        Instance i;
        final EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("PersistanceUnitDeliver");
        final EntityManager em = emf.createEntityManager();

        final EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Query query = em.createNamedQuery("findInstancesByName");
            query.setParameter("nomInstance", name);
            i = (Instance) query.getSingleResult();


            et.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            et.rollback();
            return null;
        }
        return i;
    }

    /**
     * Effectue la requête en base qui récupere la liste des noms des instances
     *
     * @return nomInstancesList
     */
    public static ArrayList<String> getListNomInstances() {
        List<Object[]> resultList;
        ArrayList<String> nomInstancesList = new ArrayList<>();
        final EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("PersistanceUnitDeliver");
        final EntityManager em = emf.createEntityManager();

        final EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Query query = em.createNamedQuery("findInstances");

            resultList = (List<Object[]>) query.getResultList();
            for (Object res : resultList) {
                nomInstancesList.add((String) res);
            }
            et.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            et.rollback();
        }
        return nomInstancesList;
    }

    /**
     * Si le nom de la nouvelle instance est deja dans la Jlist, on rajoute (i) a la fin afin de les diffrencier
     *
     * @param filename
     * @return newFileName
     */
    private String nouveauNomInstance(String filename) {
        int lastInt = 0;
        for (int i = 1; i < 20; i++) {
            if (instanceNomList.contains(filename + " (" + i + ")")) {
                lastInt = i;
            }
        }
        return filename + " (" + (lastInt + 1) + ")";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new jPanelGradient();
        panelDuBas = new JPanel();
        jLabel4 = new JLabel();
        instanceName = new JLabel();
        dmin = new JLabel();
        jlabel6 = new JLabel();
        instanceDate1 = new JLabel();
        dmax = new JLabel();
        panelDuBas2 = new JPanel();
        jlabel8 = new JLabel();
        labelNbShifts = new JLabel();
        labelNbTM = new JLabel();
        nbshift = new JLabel();
        nbtempsmorts = new JLabel();
        selecteurSolution = new JComboBox<>();
        jlabel10 = new JLabel();
        panelImportInstance = new jPanelGradient();
        btnImport = new JLabel();
        jLabel2 = new JLabel();
        jSeparator1 = new JSeparator();
        jLabel3 = new JLabel();
        jScrollPane2 = new JScrollPane();
        jList1 = new JList<>();
        paneVisutInstance = new jPanelGradient2();
        jScrollPane1 = new jScrollGradient();
        photo = new JLabel();
        panelDuHaut = new JPanel();
        panelBtnQuit = new JPanel();
        btnQuit = new JLabel();
        panelBtnReduire = new JPanel();
        btnReduire = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panelDuBas.setBackground(new Color(40, 40, 40));

        jLabel4.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setForeground(new Color(255, 255, 255));
        jLabel4.setText("Durée minimale : ");

        instanceName.setFont(new Font("Tahoma", 1, 24)); // NOI18N
        instanceName.setForeground(new Color(255, 255, 255));
        instanceName.setText("Title");

        dmin.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        dmin.setForeground(new Color(161, 166, 167));
        dmin.setText("val");

        jlabel6.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        jlabel6.setForeground(new Color(255, 255, 255));
        jlabel6.setText("Durée maximale : ");

        instanceDate1.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        instanceDate1.setForeground(new Color(161, 166, 167));
        instanceDate1.setText("Date");

        dmax.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        dmax.setForeground(new Color(161, 166, 167));
        dmax.setText("val");

        panelDuBas2.setBackground(new Color(40, 40, 40));

        jlabel8.setFont(new Font("Tahoma", 1, 36)); // NOI18N
        jlabel8.setForeground(new Color(255, 255, 255));
        jlabel8.setText("Aucune instance sélectionnée");

        GroupLayout panelDuBas2Layout = new GroupLayout(panelDuBas2);
        panelDuBas2.setLayout(panelDuBas2Layout);
        panelDuBas2Layout.setHorizontalGroup(
                panelDuBas2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelDuBas2Layout.createSequentialGroup()
                                .addGap(658, 658, 658)
                                .addComponent(jlabel8)
                                .addContainerGap(707, Short.MAX_VALUE))
        );
        panelDuBas2Layout.setVerticalGroup(
                panelDuBas2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelDuBas2Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(jlabel8)
                                .addContainerGap(123, Short.MAX_VALUE))
        );

        labelNbShifts.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        labelNbShifts.setForeground(new Color(255, 255, 255));
        labelNbShifts.setText("Nombre de shifts :");

        labelNbTM.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        labelNbTM.setForeground(new Color(255, 255, 255));
        labelNbTM.setText("Temps morts :");

        nbshift.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        nbshift.setForeground(new Color(161, 166, 167));
        nbshift.setText("_ _ _");

        nbtempsmorts.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        nbtempsmorts.setForeground(new Color(161, 166, 167));
        nbtempsmorts.setText("_ _ _");

        selecteurSolution.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        selecteurSolution.setForeground(new Color(51, 51, 51));
        selecteurSolution.setModel(new DefaultComboBoxModel<>(new String[]{"Aucune", "Triviale", "Simple", "Optimisée 1","Optimisée 2"}));
        selecteurSolution.setBorder(null);
        selecteurSolution.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(ItemEvent evt) {

                selecteurSolutionItemStateChanged(evt);
            }
        });

        jlabel10.setFont(new Font("Tahoma", 0, 24)); // NOI18N
        jlabel10.setForeground(new Color(255, 255, 255));
        jlabel10.setText("Solution visualisée :");

        GroupLayout panelDuBasLayout = new GroupLayout(panelDuBas);
        panelDuBas.setLayout(panelDuBasLayout);
        panelDuBasLayout.setHorizontalGroup(
                panelDuBasLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(panelDuBas2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelDuBasLayout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(panelDuBasLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(instanceDate1, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(instanceName, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelDuBasLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(jlabel6))
                                .addGap(18, 18, 18)
                                .addGroup(panelDuBasLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(dmin, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(dmax, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
                                .addGap(101, 101, 101)
                                .addComponent(jlabel10, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selecteurSolution, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)
                                .addGap(110, 110, 110)
                                .addComponent(labelNbShifts, GroupLayout.PREFERRED_SIZE, 206, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nbshift, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(labelNbTM, GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nbtempsmorts, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelDuBasLayout.setVerticalGroup(
                panelDuBasLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelDuBasLayout.createSequentialGroup()
                                .addComponent(panelDuBas2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelDuBasLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(panelDuBasLayout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addGroup(panelDuBasLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(instanceName)
                                                        .addComponent(jLabel4)
                                                        .addComponent(dmin))
                                                .addGap(18, 18, 18)
                                                .addGroup(panelDuBasLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jlabel6)
                                                        .addComponent(dmax)
                                                        .addComponent(instanceDate1)))
                                        .addGroup(panelDuBasLayout.createSequentialGroup()
                                                .addGap(44, 44, 44)
                                                .addGroup(panelDuBasLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(labelNbShifts)
                                                        .addComponent(labelNbTM)
                                                        .addComponent(nbshift)
                                                        .addComponent(nbtempsmorts)
                                                        .addComponent(jlabel10)
                                                        .addComponent(selecteurSolution, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(126, Short.MAX_VALUE))
        );

        panelImportInstance.setBackground(new Color(153, 255, 204));

        btnImport.setIcon(new ImageIcon("ressources/btn_import.png")); // NOI18N
        btnImport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnImport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImportMouseClicked(evt);
            }
        });

        jLabel2.setIcon(new ImageIcon("ressources/deliver2i.png")); // NOI18N

        jSeparator1.setForeground(new Color(161, 166, 167));

        jLabel3.setBackground(new Color(161, 166, 167));
        jLabel3.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setForeground(new Color(204, 204, 204));
        jLabel3.setText("Nom");

        jScrollPane2.setBorder(null);

        jList1.setBackground(new Color(20, 30, 40));
        jList1.setFont(new Font("Tahoma", 0, 14)); // NOI18N
        jList1.setForeground(new Color(161, 166, 167));
        jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        GroupLayout panelImportInstanceLayout = new GroupLayout(panelImportInstance);
        panelImportInstance.setLayout(panelImportInstanceLayout);
        panelImportInstanceLayout.setHorizontalGroup(
                panelImportInstanceLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, panelImportInstanceLayout.createSequentialGroup()
                                .addGroup(panelImportInstanceLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(panelImportInstanceLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 335, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(GroupLayout.Alignment.LEADING, panelImportInstanceLayout.createSequentialGroup()
                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(panelImportInstanceLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jSeparator1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 335, GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, panelImportInstanceLayout.createSequentialGroup()
                                                                .addGroup(panelImportInstanceLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(btnImport))
                                                                .addGap(17, 17, 17))))
                                        .addGroup(GroupLayout.Alignment.LEADING, panelImportInstanceLayout.createSequentialGroup()
                                                .addGap(73, 73, 73)
                                                .addComponent(jLabel2)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        panelImportInstanceLayout.setVerticalGroup(
                panelImportInstanceLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelImportInstanceLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jLabel2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnImport)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 658, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        paneVisutInstance.setBackground(new Color(255, 153, 153));

        jScrollPane1.setBackground(new Color(51, 51, 51));
        jScrollPane1.setBorder(null);

        photo.setIcon(new ImageIcon("ressources/middle_panel(5).png")); // NOI18N
        jScrollPane1.setViewportView(photo);

        GroupLayout paneVisutInstanceLayout = new GroupLayout(paneVisutInstance);
        paneVisutInstance.setLayout(paneVisutInstanceLayout);
        paneVisutInstanceLayout.setHorizontalGroup(
                paneVisutInstanceLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        paneVisutInstanceLayout.setVerticalGroup(
                paneVisutInstanceLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        panelDuHaut.setBackground(new Color(29, 45, 47));

        panelBtnQuit.setBackground(new Color(29, 45, 47));

        btnQuit.setForeground(new Color(255, 51, 51));
        btnQuit.setIcon(new ImageIcon("ressources/cross.png")); // NOI18N
        btnQuit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnQuit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnQuitMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnQuitMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnQuitMouseExited(evt);
            }
        });

        GroupLayout panelBtnQuitLayout = new GroupLayout(panelBtnQuit);
        panelBtnQuit.setLayout(panelBtnQuitLayout);
        panelBtnQuitLayout.setHorizontalGroup(
                panelBtnQuitLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelBtnQuitLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(btnQuit)
                                .addContainerGap(13, Short.MAX_VALUE))
        );
        panelBtnQuitLayout.setVerticalGroup(
                panelBtnQuitLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(btnQuit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelBtnReduire.setBackground(new Color(29, 45, 47));

        btnReduire.setForeground(new Color(255, 51, 51));
        btnReduire.setIcon(new ImageIcon("ressources/line.png")); // NOI18N
        btnReduire.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReduire.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReduireMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnReduireMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnReduireMouseExited(evt);
            }
        });

        GroupLayout panelBtnReduireLayout = new GroupLayout(panelBtnReduire);
        panelBtnReduire.setLayout(panelBtnReduireLayout);
        panelBtnReduireLayout.setHorizontalGroup(
                panelBtnReduireLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelBtnReduireLayout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(btnReduire, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(13, Short.MAX_VALUE))
        );
        panelBtnReduireLayout.setVerticalGroup(
                panelBtnReduireLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(btnReduire, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        GroupLayout panelDuHautLayout = new GroupLayout(panelDuHaut);
        panelDuHaut.setLayout(panelDuHautLayout);
        panelDuHautLayout.setHorizontalGroup(
                panelDuHautLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, panelDuHautLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(panelBtnReduire, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelBtnQuit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        panelDuHautLayout.setVerticalGroup(
                panelDuHautLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(panelBtnQuit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(panelBtnReduire, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(panelImportInstance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(paneVisutInstance, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelDuHaut, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(panelDuBas, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(panelDuHaut, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(paneVisutInstance, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(panelImportInstance, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelDuBas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    /**
     * lance la fonction choisirCSV();
     * @param evt
     */
    private void btnImportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImportMouseClicked
        choisirCSV();
    }//GEN-LAST:event_btnImportMouseClicked

    private void btnQuitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnQuitMouseClicked
        dispose();
    }//GEN-LAST:event_btnQuitMouseClicked

    /**
     * visualise l'instance selected et remet dans l'état initial
     * @param evt
     */
    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged

        if (jList1.getSelectedValue() != null) {
            visualiserInstance(jList1.getSelectedValue());
            selecteurSolution.setSelectedIndex(0);
            photo.setIcon(new ImageIcon("ressources/middle_panel(5).png"));
        }


    }//GEN-LAST:event_jList1ValueChanged

    private void btnQuitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnQuitMouseEntered
        panelBtnQuit.setBackground(Color.RED);
    }//GEN-LAST:event_btnQuitMouseEntered

    private void btnQuitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnQuitMouseExited
        panelBtnQuit.setBackground(new Color(29, 45, 47));
    }//GEN-LAST:event_btnQuitMouseExited

    private void btnReduireMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReduireMouseClicked
        setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_btnReduireMouseClicked

    private void btnReduireMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReduireMouseEntered
        panelBtnReduire.setBackground(new Color(80, 80, 80));
    }//GEN-LAST:event_btnReduireMouseEntered

    private void btnReduireMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReduireMouseExited
        panelBtnReduire.setBackground(new Color(29, 45, 47));
    }//GEN-LAST:event_btnReduireMouseExited


    /**
     * Changement d'images et d'infos a chaque changement d'état du sélecteur
     * @param evt
     */
    private void selecteurSolutionItemStateChanged(ItemEvent evt) {//GEN-FIRST:event_selecteurSolutionItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            Solution s = new Solution(instanceVisu);
            switch (selecteurSolution.getSelectedIndex()) {
                case 0:
                    photo.setIcon(new ImageIcon("ressources/middle_panel(5).png"));
                    jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                    jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    labelNbShifts.setVisible(false);
                    labelNbTM.setVisible(false);
                    nbtempsmorts.setVisible(false);
                    nbshift.setVisible(false);
                    break;
                case 1:
                    photo.setIcon(new ImageIcon("solution_ressources_draws/" + instanceVisu.getNom() + "/solution_v0" + ".png"));
                    s.findSolutionTriviale();
                    afficherInfosSolution(s);
                    break;
                case 2:
                    photo.setIcon(new ImageIcon("solution_ressources_draws/" + instanceVisu.getNom() + "/solution_v1" + ".png"));
                    s.findSolutionInter();
                    afficherInfosSolution(s);
                    break;
                case 3:
                    photo.setIcon(new ImageIcon("solution_ressources_draws/" + instanceVisu.getNom() + "/solution_v2" + ".png"));
                    s.findSolutionv3();
                    afficherInfosSolution(s);
                    break;
                case 4:
                    photo.setIcon(new ImageIcon("solution_ressources_draws/" + instanceVisu.getNom() + "/solution_v3" + ".png"));
                    s.findSolutionv4();
                    afficherInfosSolution(s);
                    break;

            }
        }
    }//GEN-LAST:event_selecteurSolutionItemStateChanged


    /**
     * affiche les infos de la solution en cours de visualisation
     * @param s
     */
    private void afficherInfosSolution(Solution s) {
        labelNbShifts.setVisible(true);
        labelNbTM.setVisible(true);
        nbtempsmorts.setVisible(true);
        nbshift.setVisible(true);
        nbtempsmorts.setText(s.getTempsMort() + "");
        nbshift.setText(s.getShiftList().size() + "");
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }


    /**
     * fait la requête par nom pour recup l'instance et affiche ces infos
     * @param name
     */
    private void visualiserInstance(String name) {
        instanceVisu = getInstancesByName(name);
        if(instanceVisu != null){
            instanceName.setText(instanceVisu.getNom());
            dmin.setText("" + instanceVisu.getDureeMin());
            dmax.setText("" + instanceVisu.getDureeMax());
            instanceDate1.setText("" + instanceVisu.getDateStr());
            panelDuBas2.setVisible(false);
        }


    }

    /**
     * select une instance de la jlist en fonction du nom et montre aucune solution
     * @param name
     */
    private void setSelectedInstance(String name) {
        if (name != null) {
            jList1.setSelectedValue(name, true);
            selecteurSolution.setSelectedIndex(0);
        }
    }


    /**
     * Ouvre et lit le fichier d'instance et ensuite calcule les 4 versions de la solution, l'enregistre grâce à la persistance via l'instance.
     * enregistre aussi les 4 images de visualisation de la solution
     * @param cheminFichierInstance
     */
    private void importerInstance(String cheminFichierInstance) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PersistanceUnitDeliver");
        EntityManager em = emf.createEntityManager();

        try {
            final EntityTransaction et = em.getTransaction();
            et.begin();
            InstanceReader reader = new InstanceReader(cheminFichierInstance);
            Instance i = reader.readInstance();

            if (instanceNomList.contains(i.getNom())) {
                i.setNom(nouveauNomInstance(i.getNom()));
                instanceNomList.add(i.getNom());
            } else {
                instanceNomList.add(i.getNom());
            }
            Solution solution1 = new Solution(i);
            Solution solution2 = new Solution(i);
            Solution solution3 = new Solution(i);
            Solution solution4 = new Solution(i);
            solution1.findSolutionTriviale();
            solution2.findSolutionInter();
            solution3.findSolutionv3();
            solution4.findSolutionv4();
            SolutionVisualization s0 = new SolutionVisualization(solution1);
            SolutionVisualization s1 = new SolutionVisualization(solution2);
            SolutionVisualization s2 = new SolutionVisualization(solution3);
            SolutionVisualization s3 = new SolutionVisualization(solution4);
            try {
                s0.saveImage(0);
                s1.saveImage(1);
                s2.saveImage(2);
                s3.saveImage(3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            remplirListeNomInstances();
            em.persist(i);
            et.commit();

            setSelectedInstance(i.getNom());
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(this, "Assurez-vous d'avoir sélectionné un fichier",
                    "Attention !", JOptionPane.WARNING_MESSAGE);
        } catch (TourneeException e) {
            e.printStackTrace();
        }
        photo.setIcon(new ImageIcon("ressources/middle_panel(5).png"));
    }

    /**
     * Cette fonction remplit la jList en récupérant les noms des instances depuis la base de données.
     */
    private void remplirListeNomInstances() {
        DefaultListModel model = new DefaultListModel();
        instanceNomList.sort(Comparator.naturalOrder()); //ordre naturel == ordre alphabétique pour les strings
        for (String instanceName : instanceNomList) {
            model.addElement(instanceName);
        }
        jList1.setModel(model);

    }

    /**
     * Cette fonction change l'image principale (ex : le camion) en un gif de chargement et ouvre un JFILEChooser permettant d'importer uniquement
     * les fichiers d'instances
     */
    private void choisirCSV() {
        photo.setIcon(new ImageIcon("ressources/loadingGIF.gif"));
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JFileChooser fileChooser = new JFileChooser("instances");
        int val = fileChooser.showOpenDialog(null); //centre le FileChooser

        if (val == JFileChooser.APPROVE_OPTION) {

            File f = fileChooser.getSelectedFile();
            String filename = f.getName();

            if (filename.endsWith(".csv")) {
                importerInstance(f.getAbsolutePath()); //si le fic est en .csv on peut importer
            } else {
                photo.setIcon(new ImageIcon("ressources/middle_panel(5).png"));
                JOptionPane.showMessageDialog(this, "Assurez-vous qu'il s'agit bien d'un fichier au format csv (.csv)",
                        "Attention !", JOptionPane.WARNING_MESSAGE);
            }

        } else{
            photo.setIcon(new ImageIcon("ressources/middle_panel(5).png"));
        }


    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Deliver2i.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Deliver2i.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Deliver2i.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Deliver2i.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Deliver2i().setVisible(true);
            }
        });


    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel btnImport;
    private JLabel btnQuit;
    private JLabel btnReduire;
    private JLabel dmax;
    private JLabel dmin;
    private JLabel instanceDate1;
    private JLabel instanceName;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JList<String> jList1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JSeparator jSeparator1;
    private JLabel jlabel10;
    private JLabel jlabel6;
    private JLabel jlabel8;
    private JLabel labelNbShifts;
    private JLabel labelNbTM;
    private JLabel nbshift;
    private JLabel nbtempsmorts;
    private JPanel paneVisutInstance;
    private JPanel panelBtnQuit;
    private JPanel panelBtnReduire;
    private JPanel panelDuBas;
    private JPanel panelDuBas2;
    private JPanel panelDuHaut;
    private JPanel panelImportInstance;
    private JLabel photo;
    private JComboBox<String> selecteurSolution;
    // End of variables declaration//GEN-END:variables
}
