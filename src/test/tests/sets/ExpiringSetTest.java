/*
 */
package tests.sets;

import tests.sets.ExpiringSet;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kitskub
 */
public class ExpiringSetTest {
    
    public ExpiringSetTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of size method, of class ExpiringSet.
     */
    @Test
    public void test1() {
        ExpiringSet instance = new ExpiringSet(2, TimeUnit.SECONDS);
        instance.add(1);
        instance.add(2);
        int expResult = 2;
        int result = instance.size();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of size method, of class ExpiringSet.
     */
    @Test
    public void testExpireLong() throws InterruptedException {
        ExpiringSet instance = new ExpiringSet(2, TimeUnit.SECONDS);
        instance.add(1);
        instance.add(2);
        Thread.sleep(2500);
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of size method, of class ExpiringSet.
     */
    @Test
    public void testExpireShort() throws InterruptedException {
        ExpiringSet instance = new ExpiringSet(2, TimeUnit.MILLISECONDS);
        instance.add(1);
        instance.add(2);
        Thread.sleep(5);
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of size method, of class ExpiringSet.
     */
    @Test
    public void testNotExpireLong() throws InterruptedException {
        ExpiringSet instance = new ExpiringSet(2, TimeUnit.SECONDS);
        instance.add(1);
        instance.add(2);
        Thread.sleep(1500);
        assertEquals(2, instance.size());
        Thread.sleep(1000);
        assertEquals(0, instance.size());
    }
    
    /**
     * Test of size method, of class ExpiringSet.
     */
    @Test
    public void testNotExpireShort() throws InterruptedException {
        ExpiringSet instance = new ExpiringSet(10, TimeUnit.MILLISECONDS);
        instance.add(1);
        instance.add(2);
        Thread.sleep(1);
        assertEquals(2, instance.size());
        Thread.sleep(15);
        assertEquals(0, instance.size());
    }
    
    /**
     * Test of size method, of class ExpiringSet.
     */
    @Test
    public void testComplex() throws InterruptedException {
        ExpiringSet instance = new ExpiringSet(200, TimeUnit.MILLISECONDS);
        instance.add(1);
        instance.add(2);
        Thread.sleep(110);
        assertEquals(2, instance.size());

        instance.add(3);
        assertEquals(3, instance.size());

        Thread.sleep(110);
        assertEquals(1, instance.size());

        Thread.sleep(110);
        assertEquals(0, instance.size());
    }
    
    /**
     * Test of size method, of class ExpiringSet.
     */
    @Test
    public void testOverlapping() throws InterruptedException {
        ExpiringSet instance = new ExpiringSet(200, TimeUnit.MILLISECONDS);
        instance.add(1);
        Thread.sleep(110);
        instance.add(1);
        assertEquals(1, instance.size());
        Thread.sleep(110);
        assertEquals(1, instance.size());
        Thread.sleep(110);
        assertEquals(0, instance.size());
    }
    
}