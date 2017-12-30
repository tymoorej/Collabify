package com.collabify.collabify;

import java.util.Comparator;

/**
 * Created by gillianpierce on 2017-11-18.
 */

public class VoteComparator implements Comparator<Song> {

    @Override
    public int compare(Song r1, Song r2) {
        return Integer.compare(r2.getVotes(), r1.getVotes());
    }
}
