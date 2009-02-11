/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;


import static com.mysema.query.collections.MiniApi.from;
import static com.mysema.query.collections.MiniApi.reject;
import static com.mysema.query.collections.MiniApi.select;
import static com.mysema.query.grammar.Grammar.gt;
import static com.mysema.query.grammar.GrammarWithAlias.$;
import static com.mysema.query.grammar.GrammarWithAlias.alias;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.collections.Domain.QCat;
import com.mysema.query.grammar.GrammarWithAlias;
import com.mysema.query.grammar.QMath;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path.PEntity;

/**
 * MiniApiTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class MiniApiTest {
    
    List<Cat> cats = Arrays.asList(new Cat("Kitty"), new Cat("Bob"), new Cat("Alex"), new Cat("Francis"));

    List<Integer> myInts = new ArrayList<Integer>();   
    
    List<Integer> ints = new ArrayList<Integer>();
    
    @Before public void setUp(){
        myInts.add(1);
        myInts.add(2);
        myInts.add(3);
        myInts.add(4);        
        
        GrammarWithAlias.resetAlias();
    }
    
    @Test
    public void testVarious(){
        for(Object[] strs : from($("a"), "aa","bb","cc").from($("b"), "a","b")
                .where($("a").startsWith($("b")))
                .iterate($("a"),$("b"))){
            System.out.println(Arrays.asList(strs));
        }
    }

    @Test
    public void testVarious1(){
        for(String s : from($("str"), "a","ab","cd","de")
                .where($("str").startsWith("a"))
                .iterate($("str"))){
            assertTrue(s.equals("a") || s.equals("ab"));
            System.out.println(s);
        }
    }
    
    @Test
    public void testVarious2(){
        for (Object o : from($(),1,2,"abc",5,3).where($().ne("abc")).iterate($())){
            int i = (Integer)o;
            assertTrue(i > 0 && i < 6);
            System.out.println(o);
        }                
    }
    
    @Test
    public void testVarious3(){
        for (Integer i : from($(0),1,2,3,4).where($(0).lt(4)).iterate($(0))){
            System.out.println(i);
        }
    }
    
    @Test
    public void testAlias(){
        // 1st
        QCat cat = new QCat("cat");  
        for (String name : from(cat,cats).where(cat.kittens.size().gt(0))
                          .iterate(cat.name)){
            System.out.println(name);
        }        
        
        // 1st - variation 1        
        for (String name : from(cat,cats).where(gt(cat.kittens.size(),0))
                          .iterate(cat.name)){
            System.out.println(name);
        }        
        
        // 2nd
        Cat c = alias(Cat.class, "cat");
        for (String name : from(c,cats).where($(c.getKittens()).size().gt(0))
                          .iterate($(c.getName()))){
            System.out.println(name);
        }
        
        // 2nd - variation 1
        for (String name : from(c,cats).where($(c.getKittens().size()).gt(0))
                          .iterate($(c.getName()))){
            System.out.println(name);
        }                
                            
    }
    
    @Test
    public void testAlias2(){        
        // 1st
        QCat cat = new QCat("cat");  
        for (String name : from(cat,cats).where(cat.name.like("fri%"))
                          .iterate(cat.name)){
            System.out.println(name);
        }
        
        // 2nd
        Cat c = alias(Cat.class, "cat");        
        for (String name : from(c,cats).where($(c.getName()).like("fri%"))
                          .iterate($(c.getName()))){
            System.out.println(name);
        }      
    }
    
    @Test
    public void testAlias3(){
        new QCat("cat").birthdate.after(new Date());
        
        Cat c = alias(Cat.class, "cat");
        
        from(c,cats)
        .where($(c.getMate().getBirthdate()).after(new Date()))
        .iterate($(c)).iterator();              
    }
    
    @Test
    @Ignore
    public void testAlias4(){        
        Cat c = alias(Cat.class, "cat");
        
        // TODO : FIXME : Janino compiler doesn't handle generic collections
        from(c,cats)
        .where($(c.getKittens().get(0).getBodyWeight()).gt(12))
        .iterate($(c.getName())).iterator();
    }
    
    @Test
    public void testAlias5(){
        Cat c = alias(Cat.class, "cat");
        Cat other = new Cat();
        
        from(c,cats)
        .where($(c).eq(other))
        .iterate($(c)).iterator();
    }
    
    @Test
    public void testAlias6(){        
        new QCat("cat").kittens.contains(new QCat("other"));
        
        Cat c = alias(Cat.class, "cat");
        Cat other = new Cat();
        
        from(c,cats)
        .where($(c.getKittens().contains(other)))
        .iterate($(c)).iterator();
    }
    
    @Test
    public void testAlias7(){
        Cat c = alias(Cat.class, "cat");
        
        from(c,cats)
        .where($(c.getKittens().isEmpty()))
        .iterate($(c)).iterator();
    }
    
    @Test
    public void testAlias8(){
        Cat c = alias(Cat.class, "cat");
        
        from(c,cats)
        .where($(c.getMate().getName()).startsWith("B"))
        .iterate($(c)).iterator();        
    }
    
    @Test
    public void testAlias9(){
        Cat c = alias(Cat.class, "cat");
        
        from(c,cats)
        .where($(c.getMate().getName()).toUpperCase().eq("MOE"))
        .iterate($(c)).iterator();        
    }
    
    @Test
    public void testAlias10(){
        Cat c = alias(Cat.class, "cat");
        
        try{
            from(c,cats).where($(c.getMate().getName().toUpperCase()).eq("MOE"));
            fail("expected NPE");
        }catch(NullPointerException ne){
            // expected
        }
        
    }
    
    @Test
    @Ignore
    public void testAliasToString(){
        // NOTE : temporarily commented out, since alias features have been moved to querydsl-core
        Cat c = alias(Cat.class, "c");
        
        assertEquals("c", c.toString());
        assertEquals("c.getMate()", c.getMate().toString());
        assertEquals("c.getMate().getKittens().get(0)", c.getMate().getKittens().get(0).toString());
        
        assertEquals("c.getKittens().get(0)", c.getKittens().get(0).toString());
        assertEquals("c.getKittens().get(1)", c.getKittens().get(1).toString());
        assertEquals("c.getKittens().get(0).getMate()", c.getKittens().get(0).getMate().toString());        
    }
    
    @Test
    public void testMapUsage(){
        // FIXME
        Map<String,String> map = new HashMap<String,String>();      
        map.put("1","one");
        map.put("2","two");
        map.put("3","three");
        map.put("4","four");
        
        // 1st 
        PEntity<Map.Entry<String,String>> e = $(map.entrySet().iterator().next());
        for (Map.Entry<String,String> entry : from(e, map.entrySet()).iterate(e)){
            System.out.println(entry.getKey() + " > " + entry.getValue());
        }
        
        // 2nd
//        for (String[] kv : from($("k"), $("v"), map).iterate($("k"),$("v"))){
//            System.out.println(kv[0] + " > " + kv[1]);
//        }
    }
    

    @SuppressWarnings("unchecked")
    @Test public void testMathFunctions(){
        Cat c = alias(Cat.class,"c");
        Expr<Integer> i = new Expr.EConstant<Integer>(1);
        Expr<Double> d = new Expr.EConstant<Double>(1.0);
        from(c, cats)
        .iterate(
                QMath.abs(i),
                QMath.acos(d),
                QMath.asin(d),
                QMath.atan(d),
                QMath.ceil(d),
                QMath.cos(d),
                QMath.tan(d),
                QMath.sqrt(i),
                QMath.sin(d),
                QMath.round(d),
                QMath.random(),
                QMath.pow(d,d),
                QMath.min(i,i),
                QMath.max(i,i),
                QMath.mod(i,i),
                QMath.log10(d),
                QMath.log(d),
                QMath.floor(d),
                QMath.exp(d)).iterator();
          
    }
    
    @Test public void testSimpleSelect() {
    //  Iterable<Integer> threeAndFour = select(myInts, greaterThan(2));
        Iterable<Integer> threeAndFour = select(myInts, $(0).gt(2));  
        
        for (Integer i : threeAndFour) ints.add(i);
        assertEquals(Arrays.asList(3,4), ints);
    }

    @Test public void testSimpleReject() {
    //  Iterable<Integer> oneAndTwo = reject(myInts, greaterThan(2));
        Iterable<Integer> oneAndTwo = reject(myInts, $(0).gt(2));
        
        for (Integer i : oneAndTwo) ints.add(i);
        assertEquals(Arrays.asList(1,2), ints);
    }
    
    
}
