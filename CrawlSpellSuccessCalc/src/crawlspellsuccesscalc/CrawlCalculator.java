/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlspellsuccesscalc;

import org.jcp.xml.dsig.internal.dom.DOMUtils;
import sun.awt.windows.WPrinterJob;

import java.util.function.DoubleToIntFunction;

import static java.lang.Math.*;
import static java.lang.StrictMath.log10;

public class CrawlCalculator {
    
    public static final int NOSHIELD=-1;
    public static final int BIGSHIELD=0;
    public static final int SHIELD=1;
    public static final int BUCKLER=2;
    public static final int LITTLESPECIES=0;
    public static final int SMALLSPECIES=1;
    public static final int NORMALSPECIES=2;
    public static final int BIGSPECIES=3;
    
    public static final int NOVEHUMET=-1;
    public static final int NOWIZARDRY=-1;
    public static final int NOWILDMAGIC=-1;
    public static final int NOSUBDUEDMAGIC=-1;
    
    private static final int[] SPELL_DIFFICULTIES={3,15,35,70,100,150,200,260,340};
    private static final double[] EV_PENALTIES={5,3,0.8};
    private static final int[] SIZE_FACTORS={9,7,5,3};
    
    public static final int SHIELDS=0;
    public static final int ARMOR=1;
    public static final int SPC=2;
    public static final int MAG1=3;
    public static final int MAG2=4;
    public static final int MAG3=5;
    
    private double spellcasting;
    private int nskill;
    private double[] magic_skills;
    private double shields;
    private int shieldtype;
    private double armor;
    private int encumbrance;
    private int size;
    private int vehumet;
    private int wizardry;
    private boolean hat;
    private int wildmagic;
    private int subduedmagic;
    
    private int Str;
    private int Int;
    private int Dex;
    
    private int spellLvl;

    public double getSpellcasting() {
        return spellcasting;
    }

    public int getNskill() {
        return nskill;
    }

    public double[] getMagic_skills() {
        return magic_skills;
    }

    public double getShields() {
        return shields;
    }

    public int getShieldtype() {
        return shieldtype;
    }

    public double getArmor() {
        return armor;
    }

    public int getEncumbrance() {
        return encumbrance;
    }

    public int getSize() {
        return size;
    }

    public int getVehumet() {
        return vehumet;
    }

    public int getWizardry() {
        return wizardry;
    }

    public boolean isHat() {
        return hat;
    }

    public int getWildmagic() {
        return wildmagic;
    }

    public int getSubduedmagic() {
        return subduedmagic;
    }

    public int getStr() {
        return Str;
    }

    public int getInt() {
        return Int;
    }

    public int getDex() {
        return Dex;
    }

    public int getSpellLvl() {
        return spellLvl;
    }

    public boolean getHat() {
        return hat;
    }
    
    public CrawlCalculator(double spellcasting, int nskill, double[] magic_skills, 
            int vehumet, int wizardry, int wildmagic, int subduedmagic, boolean hat, 
            double shields, int shieldtype, double armor, int encumbrance, int size, 
            int Str, int Int, int spellLvl) {
        this.spellcasting = spellcasting;
        this.nskill = nskill;
        this.magic_skills = magic_skills;
        this.vehumet=vehumet;
        this.wizardry=wizardry;
        this.wildmagic=wildmagic;
        this.subduedmagic=subduedmagic;
        this.hat=hat;
        this.shields = shields;
        this.shieldtype = shieldtype;
        this.armor = armor;
        this.encumbrance = encumbrance;
        this.size = size;
        this.Str = Str;
        this.Int = Int;
        this.spellLvl = spellLvl;
    }
    
    public CrawlCalculator(CrawlCalculator calc)
    {
        this.spellcasting = calc.getSpellcasting();
        this.nskill = calc.getNskill();
        this.magic_skills = calc.getMagic_skills().clone();
        this.vehumet=calc.getVehumet();
        this.wizardry=calc.getWizardry();
        this.wildmagic=calc.getWildmagic();
        this.subduedmagic=calc.getSubduedmagic();
        this.hat=calc.getHat();
        this.shields = calc.getShields();
        this.shieldtype = calc.getShieldtype();
        this.armor = calc.getArmor();
        this.encumbrance = calc.getEncumbrance();
        this.size = calc.getSize();
        this.Str = calc.getStr();
        this.Int = calc.getInt();
        this.spellLvl = calc.getSpellLvl();
    }
    
    public boolean changeSkill(double change, int type)
    {
        switch(type)
        {
            case SHIELDS:
                this.shields+=change;
                if(this.shields<0-0.0001 || this.shields>27+0.0001)
                {
                    this.shields-=change;
                    return false;
                }
                break;
            case ARMOR:
                this.armor+=change;
                if(this.armor<0-0.0001 || this.armor>27+0.0001)
                {
                    this.armor-=change;
                    return false;
                }
                break;
            case SPC:
                this.spellcasting+=change;
                if(this.spellcasting<0-0.0001 || this.spellcasting>27+0.0001)
                {
                    this.spellcasting-=change;
                    return false;
                }
                break;
            case MAG1:
                this.magic_skills[0]+=change;
                if(this.magic_skills[0]<0-0.0001 || this.magic_skills[0]>27+0.0001)
                {
                    this.magic_skills[0]-=change;
                    return false;
                }
                break;
            case MAG2:
                this.magic_skills[1]+=change;
                if(this.magic_skills[1]<0-0.0001 || this.magic_skills[1]>27+0.0001)
                {
                    this.magic_skills[1]-=change;
                    return false;
                }
                break;
            case MAG3:
                this.magic_skills[2]+=change;
                if(this.magic_skills[2]<0-0.0001 || this.magic_skills[2]>27+0.0001)
                {
                    this.magic_skills[2]-=change;
                    return false;
                }
                break;
        }
        return true;
    }
    
    public double getSkillByType(int type)
    {
        switch(type)
        {
            case SHIELDS:
                return shields;
            case ARMOR:
                return armor;
            case SPC:
                return spellcasting;
            default:
                return magic_skills[type-MAG1];
        }
    }
    
    private double GetSpellFailure(double spellcasting, int nskill, double[] magic_skills, double armor, double shields)
    {
        double spellFailure=-1;
        double spellSkills;
        double spellDifficulty;
        double armor_shield_pen;
        
        /*double spellcasting=3;
        double magic_skills[]={4.8};
        int nskill=1;
        int Int=19;
        int spellLvl=4;*/
        
        int i;
        
        //Spell Skills
        double spellskill_avg=0;
        for(i=0; i<nskill; i++)
            spellskill_avg+=magic_skills[i];
        spellskill_avg/=(double)nskill;
        double s_0=((double)spellcasting)/2.0+spellskill_avg*2;

        //spellSkills=50*(log10(1+s_0/50.0)/log10(2));
        //spellSkills = stepdown_value(s_0 , 50, 50, 150, 200);
        //spellSkills=stepdown(s_0, 50, 200);
        //double test=spellSkills*6;
        
        spellDifficulty=SPELL_DIFFICULTIES[spellLvl-1];
        
        //Armor and shield penalty
        double armorpen;
        double shieldpen;
        armorpen=0.4 * ((double)encumbrance*encumbrance) * (45.0 - (double)armor) / 45.0 / ((double)(Str + 3));
        if(shieldtype!=NOSHIELD)
            shieldpen=EV_PENALTIES[shieldtype] - (double)shields/(double)SIZE_FACTORS[size];
        else
            shieldpen=0;
        if(shieldpen<0)
            shieldpen=0;
        armor_shield_pen=19*armorpen+19*shieldpen;
        //armor_shield_pen=0;
        
                
        spellFailure=60-s_0*6-Int*2+spellDifficulty+armor_shield_pen;
        return spellFailure;
    }
    
    public double GetSpellFailure()
    {
        return GetSpellFailure(spellcasting, nskill, magic_skills, armor, shields);
    }
    
    private double GetRawChance(double spellcasting, int nskill, double[] magic_skills, double armor, double shields)
    {
        
        double spellFailure=GetSpellFailure(spellcasting, nskill, magic_skills, armor, shields);
        //step down (ХРЕН ЕГО ЗНАЕТ КАКАЯ ТАМ РЕАЛЬНО ФОРМУЛА), но нарыл таблицу
        /*
        {45, 45}, {42, 43}, {38, 41}, {35, 40}, {32, 38}, {28, 36},
        {22, 34}, {16, 32}, {10, 30}, {2, 28}, {-7, 26}, {-12, 24},
        {-18, 22}, {-24, 20}, {-30, 18}, {-38, 16}, {-46, 14},
        {-60, 12}, {-80, 10}, {-100, 8}, {-120, 6}, {-140, 4},
        {-160, 2}, {-180, 0}
                */
       /* double rawChance=-1;
        
        int i;
        
        int stepDownTable[][]={{45, 45}, {42, 43}, {38, 41}, {35, 40}, {32, 38}, {28, 36},
        {22, 34}, {16, 32}, {10, 30}, {2, 28}, {-7, 26}, {-12, 24},
        {-18, 22}, {-24, 20}, {-30, 18}, {-38, 16}, {-46, 14},
        {-60, 12}, {-80, 10}, {-100, 8}, {-120, 6}, {-140, 4},
        {-160, 2}, {-180, 0}};
        i=0;
        if(spellFailure>45)
            rawChance=spellFailure;
        else
            while(i<stepDownTable.length && spellFailure<stepDownTable[i][0])
            {
                rawChance=stepDownTable[i][1];
                i++;
            }*/

       double rawChance =  max((((spellFailure + 426) * spellFailure + 82670) * spellFailure + 7245398)
               / 262144, 0);

        
        if(wildmagic!=NOWILDMAGIC)
            rawChance+=wildmagic*7;
        if(subduedmagic!=NOSUBDUEDMAGIC)
            rawChance-=subduedmagic*7;
        if(hat)
            rawChance+=7;
        
        double chanceModifier=1;
        if(vehumet!=NOVEHUMET && vehumet>=3)
            chanceModifier*=2.0/3.0;
        if(wizardry!=NOWIZARDRY)
            chanceModifier=chanceModifier * 6.0 / (7.0 + wizardry);
        if(chanceModifier<0.5)
            chanceModifier=0.5;
        rawChance*=chanceModifier;
        System.out.println(rawChance);
        return rawChance;

    }
    
    public double GetRawChance()
    {
        return GetRawChance(spellcasting, nskill, magic_skills, armor, shields);
    }
    
    public double calculateMiscast(double spellcasting, int nskill, double[] magic_skills, double armor, double shields)
    {
        double rawChance=GetRawChance(spellcasting,nskill,magic_skills,armor,shields);
        System.out.println("raw is "+rawChance);
        if(rawChance>=100)
            return 100;
        else if(rawChance<=0)
            return 0;
        else return (int)(get_true_fail_rate((int)rawChance)*100);
    }
    
    public double calculateMiscast()
    {
       /* double raw_fail = GetRawChance();*/
        double chance = calculateMiscast(spellcasting, nskill, magic_skills, armor, shields);
       /* System.out.println(chance);
        int severity;
        if(chance<1)
            severity=0;
        else if(chance<5)
            severity=1;
        else if(chance<25)
            severity=2;
        else severity=3;
        int level = spellLvl;
        System.out.println("Spell lvl is:"+level);
        double C = 70000.0 / (150 * level * (10 + level));
        int k = severity + 1;
        System.out.println("C*K is:"+C*k);
        System.out.println(raw_fail);
        System.out.println("Severity is:" + severity);
        if(C*k <= raw_fail)
            chance = 0.0;
        else return chance;
        while ((C * k) <= raw_fail)
        {
            System.out.println("C*K is:"+C*k);
            double newraw = (raw_fail + 1 - (C * k));
            System.out.println("newraw is:"+newraw);
            chance += get_true_fail_rate( (int)newraw)
                    * severity / (k * (k - 1));
            System.out.println("Chance is:"+chance);
            k++;
            System.out.println("K is:"+k);
        }
        System.out.println(chance);*/
        return chance;
    }
    
    
    //Дальнейшие функции скопированы из исходников стоунсупа, я ничерта в них не шарю
    private double stepdown(double value, double step)
    {
        return step * (log10(1 + value / step)/log10(2));
    }
    private double stepdown(double value, int step, int max)
    {
        double ret = stepdown((double) value, (double)step);

        if (max > 0 && ret > max)
            return max;

        return ret - 0.5;
    }

    // Deprecated definition. Call directly stepdown instead.
//    private double stepdown_value(double base_value, int stepping, int first_step,int a,
//                        int ceiling_value)
//    {
//        // Disabling max used to be -1.
//        if (ceiling_value < 0)
//            ceiling_value = 0;
//
//        if (ceiling_value>0 && ceiling_value < first_step)
//            return min(base_value, ceiling_value);
//        if (base_value < first_step)
//            return base_value;
//
//        int diff = first_step - stepping;
//        // Since diff < first_step, we can assume here that ceiling_value > diff
//        // or ceiling_value == 0.
//        return diff + stepdown(base_value - diff, stepping,
//                               ceiling_value>0 ? ceiling_value - diff : 0);
//    }

    static int _tetrahedral_number(int n)
    {
        return n * (n+1) * (n+2) / 6;
    }
    static double get_true_fail_rate(int raw_fail)
    {
        final int outcomes = 101 * 101 * 100;
        final int target = raw_fail * 3;

        if (target <= 100)
        {
            // The failures are exactly the triples of nonnegative integers
            // that sum to < target.
            return (double)(_tetrahedral_number(target)) / outcomes;
        }
        if (target <= 200)
        {
            // Some of the triples that sum to < target would have numbers
            // greater than 100, or a last number greater than 99, so aren't
            // possible outcomes. Apply the principle of inclusion-exclusion
            // by subtracting out these cases. The set of triples with first
            // number > 100 is isomorphic to the set of triples that sum to
            // 101 less; likewise for the second and third numbers (100 less
            // in the last case). Two or more out-of-range numbers would have
            // resulted in a sum of at least 201, so there is no overlap
            // among the three cases we are subtracting. 1335501 - 2 * 162239 - 167200 / 1020100
            return (double)(_tetrahedral_number(target)
                          - 2 * _tetrahedral_number(target - 101)
                          - _tetrahedral_number(target - 100)) / outcomes;
        }
        // The random2avg distribution is symmetric, so the last interval is
        // essentially the same as the first interval.
        return (double)(outcomes - _tetrahedral_number(300 - target)) / outcomes;
    }
}
