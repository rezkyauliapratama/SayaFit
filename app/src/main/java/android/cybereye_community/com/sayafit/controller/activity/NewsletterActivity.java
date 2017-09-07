package android.cybereye_community.com.sayafit.controller.activity;

import android.cybereye_community.com.sayafit.R;
import android.cybereye_community.com.sayafit.controller.database.Facade;
import android.cybereye_community.com.sayafit.controller.database.ManageGuideTbl;
import android.cybereye_community.com.sayafit.controller.database.entity.GuideTbl;
import android.cybereye_community.com.sayafit.databinding.ActivityNewsletterBinding;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import br.tiagohm.markdownview.css.InternalStyleSheet;
import br.tiagohm.markdownview.css.styles.Github;

/**
 * Created by User on 23/3/2017.
 */

public class NewsletterActivity extends BaseActivity {

    public final static String ID = "ID";

    ActivityNewsletterBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_newsletter);

        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Guideline");
        }

        long id = getIntent().getLongExtra(ID, -1);

        ManageGuideTbl dao = Facade.getInstance().getManageGuideTbl();

        GuideTbl item = dao.get(id);

        if (item != null) {
            binding.textviewTitle.setText(item.getNm_category());
            String summary = "<html><body>"+item.getKet()+"</body></html>";
//            InternalStyleSheet css = new Github();

//            binding.markdownView.addStyleSheet(css);
//            binding.markdownView.loadMarkdown(summary);
            binding.markdownView.loadData(summary, "text/html", "UTF-8");
        }




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}