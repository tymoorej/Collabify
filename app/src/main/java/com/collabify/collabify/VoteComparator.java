package com.collabify.collabify;

import java.util.Comparator;

/**
 * Created by gillianpierce on 2017-11-18.
 */

public class VoteComparator implements Comparator<RecyclerViewClass> {

    @Override
    public int compare(RecyclerViewClass r1, RecyclerViewClass r2) {
        return Integer.compare(r2.getVotes(), r1.getVotes());
    }
}
