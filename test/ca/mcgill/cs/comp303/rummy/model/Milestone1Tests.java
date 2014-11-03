package ca.mcgill.cs.comp303.rummy.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestCard.class,
        TestDeck.class,
        TestICardSet.class,
        TestHand.class,
        TestAutoMatch.class
        })
public class Milestone1Tests {}

