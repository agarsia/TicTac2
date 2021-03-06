package tk.agarsia.tictac2.view.activities;

import java.util.ArrayList;
import java.util.Random;

import tk.agarsia.tictac2.R;
import tk.agarsia.tictac2.controller.ApplicationControl;
import tk.agarsia.tictac2.controller.ApplicationControl.GameType;
import tk.agarsia.tictac2.model.player.AbstractPlayer;
import tk.agarsia.tictac2.model.player.bot.BotRandom;
import tk.agarsia.tictac2.model.player.human.HumanLocal;
import tk.agarsia.tictac2.view.MainActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ToggleButton;

/**
 * Class for the game options activity.
 * 
 * This activity implements MainActivity, displays and handles game option
 * changes. The layout file is located at res/layout/activity_opt.xml
 * 
 * @author agarsia (Bernhard Bücherl)
 * @version 1.0
 * @since 1.0
 */
public class OptActivity extends MainActivity implements OnItemSelectedListener {

	// java.util.Random object for starting with random player
	private Random rand;

	// Spinner
	private Spinner size;
	private ArrayAdapter<CharSequence> sizeAdapter;
	private Spinner win;
	private int winstart;
	private ArrayAdapter<CharSequence> winAdapter;
	private Spinner mpr;
	private ArrayAdapter<CharSequence> mprAdapter;

	/**
	 * Custom constructor
	 * 
	 * Initializes with disabled actions andthe game settings subtitle.
	 */
	public OptActivity() {
		super(false, R.string.opt_subtitle);
		rand = new Random();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opt);

		if (!ApplicationControl.isInit())
			return; 
			
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// set start local player text to the local player name
		((RadioButton) findViewById(R.id.opt_start_me))
				.setText(ApplicationControl.getStringPref("pref_player"));

		// layout configurations depending on the current game type
		switch (ApplicationControl.getGameType()) {
		case INIT:
			// not initialized, go back to menu
			back();
			break;
		case SINGLE: // hide other player name option (we play vs a bot!)
			((RadioButton) findViewById(R.id.opt_start_other))
					.setText(getString(R.string.opt_bot));
			findViewById(R.id.opt_name_desc).setVisibility(View.GONE);
			findViewById(R.id.opt_name).setVisibility(View.GONE);
			findViewById(R.id.opt_botint_box).setVisibility(View.VISIBLE);
			findViewById(R.id.opt_quickmode_box).setVisibility(View.GONE);
			break;
		case LOCAL: // XXX (not sure if this could be removed) make sure
							// to show other player name options
			findViewById(R.id.opt_name_desc).setVisibility(View.VISIBLE);
			findViewById(R.id.opt_name).setVisibility(View.VISIBLE);
			findViewById(R.id.opt_botint_box).setVisibility(View.GONE);
			findViewById(R.id.opt_quickmode_box).setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

		// init spinners
		size = (Spinner) findViewById(R.id.opt_boardsize_id);
		win = (Spinner) findViewById(R.id.opt_winlength_id);
		mpr = (Spinner) findViewById(R.id.opt_mpr_id);

		ArrayList<CharSequence> arr = new ArrayList<CharSequence>();
		arr.add(0, "3x3 Board");
		arr.add(1, "4x4 Board");
		arr.add(2, "5x5 Board");
		arr.add(3, "6x6 Board");

		sizeAdapter = new ArrayAdapter<CharSequence>(this,
				R.layout.support_simple_spinner_dropdown_item, arr);
		size.setAdapter(sizeAdapter);

		winAdapter = new ArrayAdapter<CharSequence>(this,
				R.layout.support_simple_spinner_dropdown_item,
				new ArrayList<CharSequence>());
		win.setAdapter(winAdapter);

		mprAdapter = new ArrayAdapter<CharSequence>(this,
				R.layout.support_simple_spinner_dropdown_item,
				new ArrayList<CharSequence>());
		mpr.setAdapter(mprAdapter);

		size.setOnItemSelectedListener(this);
		win.setOnItemSelectedListener(this);
		mpr.setOnItemSelectedListener(this);

		updateSpinners();
	}

	/**
	 * Helper method to update spinners in onCreate and onItemSelected
	 */
	private void updateSpinners() {
		winAdapt(size.getSelectedItemPosition());
		mprAdapt(win.getSelectedItemPosition());
	}

	/**
	 * Helper method to update win adapter depending on size adapter value
	 * 
	 * @param i
	 *            size adapter index
	 */
	private void winAdapt(int i) {
		winAdapter.clear();
		winstart = 0;
		if (i == 0)  { // solving game parameter bug [#22]
			winAdapter.insert("3", 0);
			winstart = 1;
		}
		if (i >= 1)
			winAdapter.insert("4", winstart);
		if (i >= 2)
			winAdapter.insert("5", winstart+1);
		if (i >= 3)
			winAdapter.insert("6", winstart+2);
		winAdapter.notifyDataSetChanged();
	}

	/**
	 * Helper method to update mpr adapter depeinding on win adapter value
	 * 
	 * @param i
	 *            win adapter index
	 */
	private void mprAdapt(int i) {
		mprAdapter.clear();
		if (i >= 0)
			mprAdapter.insert("1", 0);
		if (i >= 1)
			mprAdapter.insert("2", 1);
		if (i >= 3)
			mprAdapter.insert("3", 2);
		mprAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		updateSpinners();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		updateSpinners();
	}

	@Override
	public Intent getSupportParentActivityIntent() {
		// "up" navigation to return to menu
		return new Intent(getApplicationContext(), MenuActivity.class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// add options menu next & back button to the actionbar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.opt_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// add options menu next & back button on select support
		if (item.getItemId() == R.id.action_next) {
			next();
			return true;
		} else if (item.getItemId() == R.id.action_back) {
			back();
			return true;
		}
		return false;
	}

	/**
	 * Method to go back to the menu.
	 * 
	 * Will start the MenuActivity.
	 */
	private void back() {
		ApplicationControl.start(this, MenuActivity.class);
	}

	/**
	 * Method to start the game.
	 * 
	 * This function starts the game with given settings.
	 */
	private void next() {
		// make sure the game controller is empty initialized.
		ApplicationControl.reinit();

		// fetch option views
		RadioGroup index = (RadioGroup) findViewById(R.id.opt_start);

		int interval = 0; // 0 = no quickmode 

		if(ApplicationControl.getGameType()==GameType.LOCAL&&((ToggleButton) findViewById(R.id.opt_quickmode)).isChecked()) 
			interval = Integer.parseInt(ApplicationControl.getStringPref("pref_quickmode","0"));
		
		int boardDim = 3 + size.getSelectedItemPosition(); // board dimension
															// (3x3,4x4,5x5,6x6)
		int winLength = 3 + win.getSelectedItemPosition(); // win length
															// (3,4,5,6)
		winLength += winstart==0?1:0; //bug fix
		
		int mpt = 1 + mpr.getSelectedItemPosition(); // marks per turn (1,2,3)
		int spi = 1 + rand.nextInt(2); // preinitialize start player index with
										// random

		switch (index.getCheckedRadioButtonId()) {
		case R.id.opt_start_me:
			spi = 1;
			break;
		case R.id.opt_start_other:
			spi = 2;
			break;
		}

		// init other player
		AbstractPlayer player2 = null;
		if (ApplicationControl.getGameType() == GameType.SINGLE) {
			// start singleplayer (vs Bot)
			switch(((RadioGroup) findViewById(R.id.opt_botint)).getCheckedRadioButtonId()) {
			case R.id.opt_botint_hard:
				player2 = new tk.agarsia.tictac2.model.player.bot.BotSmart(ApplicationControl.getGame());
				break;
			case R.id.opt_botint_med:
				player2 = new tk.agarsia.tictac2.model.player.bot.smartV2.BotSmart(ApplicationControl.getGame(),true,true);
				break;
			default:
				player2 = new BotRandom(ApplicationControl.getGame());				
				break;
			}
		} else if (ApplicationControl.getGameType() == GameType.LOCAL) {
			// start local (vs other local human)
			String name = ((EditText) findViewById(R.id.opt_name)).getText()
					.toString();
			if (name == null)
				name = getString(R.string.opt_default);
			else if (name.length() <= 1)
				name = getString(R.string.opt_default);

			player2 = new HumanLocal(name, ApplicationControl.getGame());
		}

		// re init local players name...
		if (ApplicationControl.getMe().getName().length() <= 1)
			ApplicationControl.getMe().setName(
					getString(R.string.pref_player_default));

		// initialize game model
		ApplicationControl.getGame().initModel(interval, boardDim, winLength,
				mpt, spi);

		// set players for the current game
		ApplicationControl.getGame().setPlayers(ApplicationControl.getMe(),
				player2);

		// start the game activity
		ApplicationControl.start(this, GameActivity.class);
	}

	@Override
	public void onClick(View v) {
	}
}