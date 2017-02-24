package org.sonar.plugins.delphi.core.helpers;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Resource;
import org.sonar.api.utils.WildcardPattern;

public class PackageResource extends Resource {

	public static final String DEFAULT_PACKAGE_NAME = "[default]";

	public PackageResource(String key) {
		super();
	    setKey(StringUtils.defaultIfEmpty(StringUtils.trim(key), DEFAULT_PACKAGE_NAME));
	}
	  
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Language getLanguage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLongName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return getKey();
	}

	@Override
	public Resource getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQualifier() {
		// TODO Auto-generated method stub
		return "PKG";
	}

	@Override
	public String getScope() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean matchFilePattern(String arg0) {
		return false;
	}

}
