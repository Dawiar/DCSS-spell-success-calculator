/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlspellsuccesscalc;

/**
 *
 * @author Игорь
 */
public class CrawlSpellSuccessCalc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainCalcFrame().setVisible(true);
                //=========>>ARGUMENTS HERE<<=============
//                double arr[]={0,0};                                  //Put your base magic skills here
//                int[] apts={0,1};                                    //Put aptitudes for the magic skills here
//                int xl=15;                                         //Your XL [OPTIONAL, leave -1 if don't need it]
//                int nextxl=88;                                     //Your xp% to the next XL [OPTIONAL, leave -1 if don't need it]
//                int xpApt=0;                                      //Your XP aptitude [OPTIONAL]
//                CrawlCalculator calc=new CrawlCalculator(
//                        0,                                             //Base spellcasting
//                        arr.length,                                               
//                        arr, 
//                        -1,                                              //Vehumet piety (-1 if you do not worship him)
//                        1,                                              //number of wizardy items (-1 if no wizardy (NOT ZERO!!!))
//                        -1,                                              //wild magic mutation rank (-1 if you don't have it)
//                        -1,                                              //subdued magic mutation rank (-1 if you don't have it)
//                        false,                                           //true if you have Hat of the High Council, false if not
//                        5.8,                                               //base shields skill                             
//                        2,                                              //shield type:
//                                                                         //-1 = NO SHIELD; 0 = BIG SHIELD; 1 = NORMAL SHIELD; 2 = BUCKLER
//                        11.4,                                               //base armor skill
//                        4,                                               //encumbrance rationg of your armor
//                        2,                                               //size of your character:
//                                                                         //0 = LITTLE (Spriggan); 1 = SMALL (Kobold, Halfling); 2 = NORMAL; 3 = BIG (Ogre, Troll, Centaur, Naga, Formicid);
//                        18,                                              //Str 
//                        11,                                              //Int
//                        6                                                //Spell level
//                );
//                XPOptimizer optimus=new XPOptimizer(calc, 
//                        10,                                              //miscast% that you want
//                        apts, 
//                        -2,                                               //Spellcasting aptitude
//                        -1,                                              //Armor aptitude 
//                        -1,                                              //Shields aptitude
//                        xl,
//                        nextxl,
//                        xpApt
//                );                                             
                //=========END OF ARGUMENTS===============
                
                //double arr2[]={26,25,25};
                //System.out.println(calc.calculateMiscast(20.7, 3, arr2, 26.6, 15));
                //System.out.println(calc.calculateMiscast());
                
                //System.out.println(calc.getArmor());
                //System.out.println(calc.getShields());
                //System.out.println(calc.getSpellcasting());
                //System.out.println(calc.getMagic_skills()[0]);
                //System.out.println(calc.getMagic_skills()[1]);
//                System.out.println();
//                if(optimus.OptimizeXP())
//                {
//                    System.out.format("Miscast: %.0f%%\n",calc.calculateMiscast());
//                    System.out.format("Armor skill: %.1f\n",calc.getArmor());
//                    System.out.format("Shields skill: %.1f\n",calc.getShields());
//                    System.out.format("Spellcasting skill: %.1f\n",calc.getSpellcasting());
//                    for(int i=0; i<calc.getMagic_skills().length; i++)
//                        System.out.format("Magic skill %d: %.1f\n", i+1,calc.getMagic_skills()[i]);
//                    if(xl!=-1)
//                    {
//                        System.out.format("You will be xl %d %d%%\n", optimus.getXl(), optimus.getNextxl());
//                    }
//                }
//                else
//                {
//                    System.out.println("Impossible to reach that miscast %");
//                    for(int i=0; i<calc.getMagic_skills().length; i++)
//                        arr[i]=27;
//                    System.out.format("Minimum: %.0f%%\n",calc.calculateMiscast(27, arr.length, arr, 27, 27));
//                }
                //System.out.format(calc.getMagic_skills()[0]);
                //System.out.format(calc.getMagic_skills()[1]);
                //System.out.format(calc.getMagic_skills()[2]);
            }
        });
    }
    
}
