package fr.smile.main;

public class HtmlCleaner {

    public String warningCleaner(String toClean) {
        return toClean.replaceAll("<li>", "\n\t- ").replaceAll("<br/>", "\n")
                .replaceAll("\\<.*?>", "");
    }
}
