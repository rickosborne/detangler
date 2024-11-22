package org.rickosborne.detangler;

import com.badlogic.gdx.graphics.Color;

public enum Theme {
    Ace(0x9e9e9eff, 0x5e1984ff, 0x3da542ff, 0xa9a9a9ff, 0xffffffff),
    King(0xfe941eff, 0xffff00ff, 0xff1e26ff),
    Queen(0xa20161ff, 0xfd9855ff, 0xd42c00ff, 0xffffffff, 0xd161a2ff),
    Jack(0xbcc4c7ff, 0xa5fa5eff),
    Ten(0xf6aab7ff, 0x55cdfdff),
    Nine(0xb57edcff, 0x4a8122ff),
    Eight(0xfcf431ff, 0x9d59d2ff),
    Seven(0xd70071ff, 0x0035aaff, 0x9c4e97ff),
    Joker(0x21b0feff, 0xfed700ff, 0xfe218bff),
    ;
    public final Color edgeIntersected;
    public final Color edgeDefault;
    public final Color edgeTouched;
    public final Color pointDefault;
    public final Color pointTouched;

    Theme(final Color edgeDefault, final Color edgeIntersected, final Color edgeTouched, final Color pointDefault, final Color pointTouched) {
        this.edgeDefault = edgeDefault;
        this.edgeIntersected = edgeIntersected;
        this.edgeTouched = edgeTouched;
        this.pointDefault = pointDefault;
        this.pointTouched = pointTouched;
    }

    Theme(final int edgeDefault, final int edgeIntersected, final int edgeTouched, final int pointDefault, final int pointTouched) {
        this(new Color(edgeDefault), new Color(edgeIntersected), new Color(edgeTouched), new Color(pointDefault), new Color(pointTouched));
    }

    Theme(final int edges, final int touched, final int points) {
        this(edges, 0x777777ff, touched, points, touched);
    }

    Theme(final int edges, final int points) {
        this(edges, 0x777777ff, 0xeeeeeeff, points, 0xffffffff);
    }

    public static Theme random() {
        final int themeCount = Theme.values().length;
        return Theme.values()[Rand.index(themeCount)];
    }
}
