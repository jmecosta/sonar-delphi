/*
 * Sonar Delphi Plugin
 * Copyright (C) 2016 Sandro Luck
 * Author: Sandro Luck(sandro.luck@gmx.ch)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.delphi.pmd;

import org.sonar.api.issue.Issue;
import org.sonar.api.issue.IssueComment;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.Duration;

import java.util.*;

public class DelphiIssue implements Issue {

    private final RuleKey ruleKey;
    private final String message;
    private final Integer line;
    List<String> tags = new ArrayList<>();

    //user other constructor this is just a work around for debugging
    public DelphiIssue(int line, RuleKey ruleKey) {
        this.line = line;
        this.tags.add("THIS IS A TAG, change thsi in code to be useful");
        this.ruleKey = ruleKey;
        this.message = "MSG is deprecated, changed for debugging";
    }

    public Issue getIssue() {
        return this;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "(" + line + ", " + ruleKey() + ")";
    }

    @Override
    public String key() {
        return ruleKey().toString() + "   This is just Rulekey.toSTring()";
    }

    @Override
    public String componentKey() {
        return null;
    }

    @Override
    public RuleKey ruleKey() {
        return this.ruleKey;
    }

    @Override
    public String language() {
        return "Delphi";
    }

    @Override
    public String severity() {
        return null;
    }

    @Override
    public String message() {
        return null;
    }

    @Override
    public Integer line() {
        return this.line;
    }

    @Override
    public Double effortToFix() {
        return null;
    }

    /**
     * Arbitrary distance to threshold for resolving the issue.
     * <br>
     * For examples:
     * <ul>
     * <li>for the rule "Avoid too complex methods" : current complexity - max allowed complexity</li>
     * <li>for the rule "Avoid Duplications" : number of duplicated blocks</li>
     * <li>for the rule "Insufficient Line Coverage" : number of lines to cover to reach the accepted threshold</li>
     * </ul>
     *
     * @since 5.5
     */
    public Double gap() {
        return null;
    }

    @Override
    public String status() {
        return null;
    }

    @Override
    public String resolution() {
        return null;
    }

    @Override
    public String reporter() {
        return null;
    }

    @Override
    public String assignee() {
        return null;
    }

    @Override
    public Date creationDate() {
        return null;
    }

    @Override
    public Date updateDate() {
        return null;
    }

    @Override
    public Date closeDate() {
        return null;
    }

    @Override
    public String attribute(String s) {
        return null;
    }

    @Override
    public Map<String, String> attributes() {
        return null;
    }

    @Override
    public String authorLogin() {
        return null;
    }

    @Override
    public String actionPlanKey() {
        return null;
    }

    @Override
    public List<IssueComment> comments() {
        return null;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public Duration debt() {
        return null;
    }

    /**
     * @since 5.5
     */
    public Duration effort() {
        return null;
    }

    @Override
    public String projectKey() {
        return null;
    }

    @Override
    public String projectUuid() {
        return null;
    }

    @Override
    public String componentUuid() {
        return null;
    }

    @Override
    public Collection<String> tags() {
        return tags;
    }
}