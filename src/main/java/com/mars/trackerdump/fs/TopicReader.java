package com.mars.trackerdump.fs;

import com.mars.trackerdump.config.SQLite;
import com.mars.trackerdump.service.TopicService;
import com.mars.trackerdump.service.TopicServiceHB;
import com.mars.trackerdump.entity.Topic;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("override")
public class TopicReader extends FSReader {

    public final static Pattern TOPIC_FNAME = Pattern.compile("([^\n]+?)category_([\\d]+?)\\.csv");
    public final static String CATEGORY_FNAME = "category_info.csv";

    @Autowired
    TopicService topicDb = new TopicServiceHB();

    final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TopicReader() {
    }

    @Nullable
    public Topic reader(String csvLine) {
        try {
            if (StringUtils.isNotBlank(csvLine)) {
                String[] a = csvLine.split(CSV_DELIM);
                if (a.length == 7) {
                    long forum_id = Long.parseLong(a[0]);
                    String forum_name = a[1];
                    long topic_id = Long.parseLong(a[2]);
                    String topic_hash = a[3];
                    String topic_name = a[4];
                    long topic_size = Long.parseLong(a[5]);
                    Date topic_date = DATE_FORMATTER.parse(a[6]); // 2007-07-05 17:23:48;
                    return new Topic(forum_id, forum_name, topic_id, topic_hash, topic_name, topic_size, topic_date);
                }
            }
        } catch (NumberFormatException | ParseException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    public <Topic> void writeDB(Topic obj) {
        topicDb.setDbName(SQLite.DB).save((com.mars.trackerdump.entity.Topic) obj);
    }

    @Override
    public boolean checkFName(@Nullable String fName) {
        return TOPIC_FNAME.matcher(fName).matches();
    }

}
