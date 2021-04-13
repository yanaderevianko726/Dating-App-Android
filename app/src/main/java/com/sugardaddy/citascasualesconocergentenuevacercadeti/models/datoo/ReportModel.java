package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;


@ParseClassName("Report")
public class ReportModel extends ParseObject {

    public static final String NO_REPORT = "no_report";

    public static final String REPORT_STOLEN_PHOTOS = "STOLEN_PHOTOS";
    public static final String REPORT_INAPPROPRIATE_CONTENT = "INAPPROPRIATE_CONTENT";
    public static final String REPORT_RUSE_OR_ABUSIVE = "RUSE_OR_ABUSIVE";
    public static final String REPORT_SPAM = "SPAM";
    public static final String REPORT_SCAM = "SCAM";

    public static final String REPORT_LIVE_LIKE = "DONT_LIKE";
    public static final String REPORT_LIVE_FRAUD = "FRAUD_OR_SCAM";
    public static final String REPORT_LIVE_SALE = "SALE_OR_SOLICITATION";
    public static final String REPORT_LIVE_RUD = "RUDENESS_OR_ABUSE";
    public static final String REPORT_LIVE_HATE = "HATE_SPEECH";
    public static final String REPORT_LIVE_NUD = "NUDITY_OR_PORNOGRAPHY";
    public static final String REPORT_LIVE_VIOLENCE = "VIOLANCE_OR_THREATS";
    public static final String REPORT_LIVE_INJURY = "HARM_OR_INJURY";

    public static final String FROM_AUTHOR = "from_author";
    public static final String TO_AUTHOR = "to_author";
    public static final String TO_LIVE_STREAM= "live_stream";
    public static final String REPORT_TYPE = "report_type";

    public User getFromAuthor() {
        return (User) getParseObject(FROM_AUTHOR);
    }

    public User getToAuthor() {
        return (User) getParseObject(TO_AUTHOR);
    }

    public void setFromAuthor(User user){
        this.put(FROM_AUTHOR, user);
    }

    public void setToAuthor(User user){
        this.put(TO_AUTHOR, user);
    }

    public void setToLiveStream(LiveStreamModel liveStream){
        this.put(TO_LIVE_STREAM, liveStream);
    }

    public void setReportType(String reportType){
        this.put(REPORT_TYPE, reportType);
    }
    ////////////////////////////////////////////////////////////////////

    public static ParseQuery<ReportModel> getQuery(){
        return ParseQuery.getQuery(ReportModel.class);
    }
}
