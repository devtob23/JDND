package com.udacity.examples.Testing;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HelperTest {


    @Test
    public void verify_getCount(){

        List<String> empNames = Arrays.asList("sareeta", "", "john","");
        assertEquals(2, Helper.getCount(empNames));
    }

    @Test
    public void verify_getState(){
        List<Integer> yrsOfExperience = Arrays.asList(13,4,15,7,17,19,1,2,3,5);
        List<Integer> expectedList = Arrays.asList(13,4,15,7,17,19,1,2,3,5);
        IntSummaryStatistics statistics = Helper.getStats(yrsOfExperience);
        assertEquals(19, statistics.getMax());

        assertEquals(expectedList, yrsOfExperience);
    }

    @Test
    public void compare_arrays(){
        int[] yrs = {10,14,2};
        int[] expectedYrs = {10,14,2};
        assertArrayEquals(expectedYrs, yrs);
    }

    @Test
    public void verify_getMerged(){
        List<String> empNames = Arrays.asList("sareeta", "bier", "john","aa");
        String empName = Helper.getMergedList(empNames);
        assertEquals("sareeta, bier, john, aa", empName);

    }
}
