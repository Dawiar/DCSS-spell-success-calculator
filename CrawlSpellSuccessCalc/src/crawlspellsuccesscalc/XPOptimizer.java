/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlspellsuccesscalc;
import static java.lang.Double.max;
import static java.lang.Double.min;
import static java.lang.Math.abs;
import static java.lang.Math.floor;
import static java.lang.Math.round;
/**
 *
 * @author Игорь
 */
public class XPOptimizer {
    
    private CrawlCalculator calc;
    private int neededMiscast;
    private int skillAptitudes[];
    private int spellcastingApt;
    private int armorApt;
    private int shieldsApt;
    private int xpAptitude;
    private int xl;

    public int getXl() {
        return xl;
    }

    public int getNextxl() {
        return nextxl;
    }
    private int nextxl;
    private long xpPool;
    
    private double baseSkills[];

    
    private final int skill_exp[] =     { 0, 50, 150, 300, 500, 750,       // 0-5
                                        1050, 1400, 1800, 2250, 2800,      // 6-10
                                        3450, 4200, 5050, 6000, 7050,      // 11-15
                                        8200, 9450, 10800, 12300, 13950,   // 16-20
                                        15750, 17700, 19800, 22050, 24450, // 21-25
                                        27000, 29750 };
    
    private final int xl_exp[]=         { 0, 10, 30, 70, 140, //1-5
                                        270, 520, 1010, 1980, 3910, //6-10
                                        7760, 15450, 26895, 45585, 72745, //11-15
                                        108375, 152475, 205045, 266085, 335595, //16-20
                                        413575, 500025, 594945, 698335, 810195, //21-25
                                        930525, 1059325 };
    
    
    private boolean calculated;


    public XPOptimizer(CrawlCalculator calc, int neededMiscast, int[] skillAptitudes, int spellcastingApt, int armorApt, int shieldsApt) {
        this.calc = calc;
        this.neededMiscast = neededMiscast;
        this.skillAptitudes = skillAptitudes;
        this.spellcastingApt = spellcastingApt;
        this.armorApt = armorApt;
        this.shieldsApt = shieldsApt;
        baseSkills=new double[skillAptitudes.length+3];
        for(int i=0; i<skillAptitudes.length+3; i++)
            baseSkills[i]=calc.getSkillByType(i);
    }
    public XPOptimizer(CrawlCalculator calc, int neededMiscast, int[] skillAptitudes, int spellcastingApt, int armorApt, int shieldsApt, int xl, int nextxl, int xpAptitude) {
        this.calc = calc;
        this.neededMiscast = neededMiscast;
        this.skillAptitudes = skillAptitudes;
        this.spellcastingApt = spellcastingApt;
        this.armorApt = armorApt;
        this.shieldsApt = shieldsApt;
        this.xpAptitude = xpAptitude;
        this.xl = xl;
        this.nextxl=nextxl;
        
        baseSkills=new double[skillAptitudes.length+3];
        for(int i=0; i<skillAptitudes.length+3; i++)
            baseSkills[i]=calc.getSkillByType(i);
        
        this.xpPool=0;
        int i;
        for(i=0; i<xl; i++)
        {
            xpPool+=AptModif(xl_exp[i], xpAptitude);
        }
        xpPool+=AptModif(xl_exp[i], xpAptitude)*(((double)nextxl) / 100.0);
    }
    
    public CrawlCalculator GetCalc() {
        return calc;
    }    
        
    /*public static void main(String[] args) {
        
        double spellcasting = 5;
        //this.nskill = nskill;
        //this.magic_skills = magic_skills;
        int nmagicSkills=2;
        double[] magicSkills=new double[nmagicSkills];
        magicSkills[0]=10;
        if(nmagicSkills>1)
            magicSkills[1]=10;
        if(nmagicSkills>2)
            magicSkills[2]=10;
        double shields = 10;
        //this.shieldtype = ShieldsSpinner;
        double armor = 10;
        int encumbrance = 7;
        //this.size = size;
        int Str = (Integer)8;
        int Int = (Integer)20;
        int spellLvl = 5;
        int vehumet=CrawlCalculator.NOVEHUMET;
        int wizardry=CrawlCalculator.NOWIZARDRY;
        int wildmagic=CrawlCalculator.NOWILDMAGIC;
        int subduedmagic=CrawlCalculator.NOSUBDUEDMAGIC;
        boolean hat=false;
        int shieldtype=CrawlCalculator.SHIELD;
        int size=CrawlCalculator.NORMALSPECIES;
        
        
        CrawlCalculator calc=new CrawlCalculator(spellcasting,nmagicSkills,magicSkills,vehumet,wizardry,wildmagic,subduedmagic,hat,shields,shieldtype,armor,encumbrance,size,Str,Int,spellLvl);
        double res=calc.calculateMiscast();
        System.out.println((new XPOptimizer(calc,10,new int[2],0,0,0)).OptimizeXP());
        System.out.println(res);
    }*/
    
    
    
    public boolean OptimizeXP()
    {
        if(calc==null)
            return false;
        if(skillAptitudes.length!=calc.getMagic_skills().length)
            return false;
        
        int i;
        
        //first, check if it's possible
        double magic_skills[]=calc.getMagic_skills().clone();
        for(i=0; i<magic_skills.length; i++)
            magic_skills[i]=27;
        if(calc.calculateMiscast(27, magic_skills.length, magic_skills, 27, 27)>neededMiscast)
            return false;
        
    
        while(calc.calculateMiscast()-neededMiscast>0.01)
        {
            int skillToLearn=selectOptimalSkill();
            //int base=((int)floor(calc.getSkillByType(skillToLearn)+0.01));
           // while(((int)floor(calc.getSkillByType(skillToLearn)+0.01))==base && abs(calc.calculateMiscast()-neededMiscast)>0.01)
            int apt= skillToLearn==CrawlCalculator.SHIELDS ? shieldsApt : (skillToLearn==CrawlCalculator.ARMOR ? armorApt : (skillToLearn==CrawlCalculator.SPC ? spellcastingApt : skillAptitudes[skillToLearn-3]));
            calc.changeSkill(0.1, skillToLearn);
            xpPool+=AptModif(skill_exp[(int)floor(calc.getSkillByType(skillToLearn))+1]*0.1, apt);
        }
        //уменьшаем скилы настолько, насколько возможно
        
        //плохой способ, да
//        for(i=0; i<2+skillAptitudes.length; i++)
//        {
//            while(neededMiscast-calc.calculateMiscast()>0.01)
//                calc.changeSkill(-0.1, i);
//            calc.changeSkill(0.1, i);
//        }
        //попробуем еще раз
        int xp,maxxp,maxj;
        maxj=-1;
        maxxp=Integer.MIN_VALUE;
        for(int j=0; j<3+skillAptitudes.length; j++)
        {
            CrawlCalculator copy=new CrawlCalculator(calc);
            for(i=0; i<3+skillAptitudes.length; i++)
            {
                int i1=(i+j)%(3+skillAptitudes.length);
                boolean changed=false;
                while(neededMiscast-copy.calculateMiscast()>-0.01 && copy.getSkillByType(i1)>=(baseSkills[i1]-0.0001) && copy.getSkillByType(i1)>=0.0001)
                {
                    copy.changeSkill(-0.1, i1);
                    changed=true;
                }
                if(changed)
                    copy.changeSkill(0.1, i1);
            }
            xp=0;
            int apt= j==CrawlCalculator.SHIELDS ? shieldsApt : (j==CrawlCalculator.ARMOR ? armorApt : (j==CrawlCalculator.SPC ? spellcastingApt : skillAptitudes[j-3]));
            for(int sk=0; sk<3+skillAptitudes.length; sk++)
            {
                for(double lvl=copy.getSkillByType(sk); lvl<calc.getSkillByType(sk); lvl+=0.1)
                    xp+=AptModif(skill_exp[(int)floor(lvl)+1]*0.1, apt);
            }
            if(xp>maxxp)
            {
                maxxp=xp;
                maxj=j;
            }
        }
        for(i=0; i<3+skillAptitudes.length; i++)
        {
            int i1=(i+maxj)%(3+skillAptitudes.length);
            int apt= i1==CrawlCalculator.SHIELDS ? shieldsApt : (i1==CrawlCalculator.ARMOR ? armorApt : (i1==CrawlCalculator.SPC ? spellcastingApt : skillAptitudes[i1-3]));
            boolean changed=false;
            while(neededMiscast-calc.calculateMiscast()>-0.01 && calc.getSkillByType(i1)>=(baseSkills[i1]-0.0001) && calc.getSkillByType(i1)>=0.0001)
            {
                xpPool-=AptModif(skill_exp[(int)floor(calc.getSkillByType(i1))+1]*0.1, apt);
                calc.changeSkill(-0.1, i1);
                changed=true;
            }
            if(changed)
            {
                xpPool+=AptModif(skill_exp[(int)floor(calc.getSkillByType(i1)-0.1)+1]*0.1, apt);
                calc.changeSkill(0.1, i1);
            }
        }
        
        if(xl!=-1 && nextxl!=-1)
        {
            xpPool-=xpPool<(AptModif(xl_exp[xl], xpAptitude)*((1.0-(double)nextxl) / 100.0)) ? xpPool : (AptModif(xl_exp[xl], xpAptitude)*((1.0-(double)nextxl) / 100.0));
            while(xpPool>0)
            {
                xl++;
                xpPool-=AptModif(xl_exp[xl], xpAptitude);
            }
            if(xl==27)
                nextxl=0;
            else
                nextxl=(int)(100-(-100)*((double)xpPool)/((double)xl_exp[xl]));
        }
        
        
        
        
        
        //пробуем простой перебор
        /*double all=(270-10*calc.getSpellcasting())*(270-10*calc.getArmor())*(270-10*calc.getShields());
        for(i=0;i<magic_skills.length;i++)
            all*=(270-10*calc.getMagic_skills()[i]);
        System.out.println(all);
        double progress=0;
        double olddif=-1;
        int n=calc.getNskill();
        for(int spc=(int)calc.getSpellcasting()*10; spc<=270; spc++)
        {
            for(int shld=(int)calc.getShields()*10; shld<=270; shld++)
            {
                for(int armr=(int)calc.getArmor()*10; armr<=270; armr++)
                {
                    for(int sk1=(int)calc.getMagic_skills()[0]*10; sk1<=270; sk1++)
                    {
                        for(int sk2=(int)(n>=2 ? calc.getMagic_skills()[1]*10 : 270); sk2<=270; sk2++)
                        {
                            for(int sk3=(int)(n>=3 ? calc.getMagic_skills()[2]*10 : 270); sk3<=270; sk3++)
                            {
                                double arr[]={sk1/10.0,sk2/10.0,sk3/10.0};
                                //if(calc.calculateMiscast(spc/10.0, n, arr, armr/10.0, shld/10.0)<=neededMiscast)
                                calc.calculateMiscast(spc/10.0, n, arr, armr/10.0, shld/10.0);
                                progress++;
                                if(progress/all-olddif>0.1)
                                {
                                    System.out.println(progress/all);
                                    olddif=progress/all;
                                }
                            }
                        }
                    }
                }
            }
        }*/
        //простой перебор не работает
        //совсем.
        
        return true;
    }
    
    private double AptModif(double exp, double apt)
    {
        //return exp*Math.pow(2.0, (-1)*apt/4);
        return exp*(1 / Math.exp(Math.log(2) * apt / 4));
    }
    
    private double GetEfficiency_rec(int skillType, int apt, CrawlCalculator calc, int depth)
    {
        double newLvl;
        double prev_chance;
        double deltaSkill;
        double NextLevelCost;
        double chance;
        double eff;
        CrawlCalculator copy=new CrawlCalculator(calc);
        newLvl=min(round(copy.getSkillByType(skillType)+(depth-1)+0.51),27);
        prev_chance=copy.GetSpellFailure();
        deltaSkill=newLvl-copy.getSkillByType(skillType);        
        NextLevelCost=0;
        for(int j=0; j<depth && (int)floor(copy.getSkillByType(skillType))+j<=27; j++)
                NextLevelCost+=AptModif(skill_exp[(int)floor(copy.getSkillByType(skillType))+j+1]*min(deltaSkill-(depth-1)+j,1), apt);
        copy.changeSkill(deltaSkill, skillType);
        boolean canIncrease=newLvl+0.001<27;
        chance=copy.GetSpellFailure();
        eff=((prev_chance-chance)/NextLevelCost)/(deltaSkill*10.0);
        if(eff==0 && canIncrease)
            return GetEfficiency_rec(skillType,apt,calc,depth+1);
        return eff;
    }
    
    private double GetEfficiency(int skillType, int apt, CrawlCalculator calc)
    {
        //copy=new CrawlCalculator(calc);
        return GetEfficiency_rec(skillType,apt,calc,1);
        
    }
    
    private int selectOptimalSkill_rec(int depth)
    {
        double maxEff=Double.MIN_VALUE;
        int effSkill=-1;
        CrawlCalculator copy;
        double eff;
        
        //shields
        if(calc.getShields()+0.01<27)
        {
            eff=GetEfficiency(CrawlCalculator.SHIELDS,shieldsApt,calc);
            if(eff>maxEff)
            {
                maxEff=eff;
                effSkill=CrawlCalculator.SHIELDS;
            }
        }
        
        //armor
        if(calc.getArmor()+0.01<27)
        {
            eff=GetEfficiency(CrawlCalculator.ARMOR,armorApt,calc);
            if(eff>maxEff)
            {
                maxEff=eff;
                effSkill=CrawlCalculator.ARMOR;
            }
        }
        
        //spc
        if(calc.getSpellcasting()+0.01<27)
        {
            eff=GetEfficiency(CrawlCalculator.SPC,spellcastingApt,calc);
            if(eff>maxEff)
            {
                maxEff=eff;
                effSkill=CrawlCalculator.SPC;
            }
        }
        
        for(int i=0; i<skillAptitudes.length; i++)
        {
            if(calc.getMagic_skills()[i]+0.01<27)
            {
                eff=GetEfficiency(CrawlCalculator.MAG1+i,skillAptitudes[i],calc);
                if(eff>maxEff)
                {
                    maxEff=eff;
                    effSkill=CrawlCalculator.MAG1+i;
                }
            }
        }
        //if(effSkill==-1)
        //    effSkill=selectOptimalSkill_rec(depth+1);
        return effSkill;
    }
    
    private int selectOptimalSkill()
    {
        return selectOptimalSkill_rec(1);
    }
}
