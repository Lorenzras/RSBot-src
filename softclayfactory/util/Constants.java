package softclayfactory.util;

import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;

public class Constants {
	public final static double VERSION = 3.311;
	
	public  final static int EDGEVILL = 45;
	public final static int BOOTH_ID = 42377;
	public final static int WELL_ID = 26945;
	public final static int CLAY_ID = 434;
	public final static int SOFTCLAY_ID = 1761;
	public final static  int[] pickAxes = {1275, 1267, 1269, 1273, 1271};
	
	public final int[] clayRocks = {10579, 10577, 10578};
	public final Tile[] bestTiles = { 
			new Tile(3083, 3399, 0),
			new Tile(3082, 3400, 0),
			new Tile(3081, 3399, 0)};

	public final static Tile[] clayPath = {
			new Tile(3067, 3505, 0), new Tile(3072, 3504, 0),
			new Tile(3077, 3503, 0), new Tile(3082, 3501, 0),
			new Tile(3086, 3500, 0), new Tile(3089, 3497, 0),
			new Tile(3089, 3492, 0), new Tile(3089, 3487, 0),
			new Tile(3092, 3486, 0), new Tile(3096, 3484, 0),
			new Tile(3099, 3482, 0), new Tile(3099, 3478, 0),
			new Tile(3099, 3474, 0), new Tile(3099, 3470, 0),
			new Tile(3099, 3465, 0), new Tile(3092, 3464, 0),
			new Tile(3088, 3465, 0), new Tile(3086, 3462, 0),
			new Tile(3087, 3459, 0), new Tile(3089, 3455, 0),
			new Tile(3088, 3449, 0), new Tile(3088, 3443, 0),
			new Tile(3088, 3437, 0), new Tile(3088, 3431, 0),
			new Tile(3086, 3426, 0), new Tile(3086, 3421, 0),
			new Tile(3086, 3416, 0), new Tile(3089, 3410, 0),
			new Tile(3088, 3406, 0), new Tile(3084, 3403, 0),
			new Tile(3081, 3398, 0) 
	};
	
	public final static Area edgevillArea = new Area(new Tile[] {
			new Tile(3062, 3510, 0), new Tile(3103, 3509, 0),
			new Tile(3102, 3487, 0), new Tile(3063, 3487, 0)
	});

	public final static Area factoryArea = new Area(new Tile[] {
			new Tile(3058, 3509, 0), new Tile(3123, 3512, 0),
			new Tile(3111, 3463, 0), new Tile(3103, 3432, 0),
			new Tile(3099, 3391, 0), new Tile(3070, 3384, 0),
			new Tile(3068, 3451, 0) 
	});

	public final static String[] DEV_PLAYERS = {
			"ichinoichi",
			"ichinoni",
			"ichinosan",
			"yongoki",
			"gogoki",
			"rokugoki",
			"Lorenzras"
	};
}
