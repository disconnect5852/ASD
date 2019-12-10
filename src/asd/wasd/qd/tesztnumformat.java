package asd.wasd.qd;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class tesztnumformat {

	public static void main(String[] args) {
		float forint=43344564564.00F;		
		float eur=545454.54F;
		String euro="EURINT";
		DecimalFormatSymbols dfs= new DecimalFormatSymbols(Locale.getDefault());
		//dfs.setDecimalSeparator(',');
		dfs.setGroupingSeparator('.');
		DecimalFormat df= new DecimalFormat("###,###.##", dfs);
		System.out.println(String.format("%s %s forintz, és %s %s", df.format(forint), euro,df.format(eur), euro));

	}

}
