package org.springframework.web.servlet.view.smart;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

/**
 * Simple downloable pdf view resolver.
 * 
 * First of all, delegates the view resolution to the injected
 * templateViewResolver. If the regular view resolution fails, it will return an
 * initialized DownloadView ready to serve the stored file by its file path.
 * 
 * This implementation suposes that all the template definitions/urls should end
 * with "_pdf"
 * 
 * @author kpacha
 */
public class SmartViewResolver extends UrlBasedViewResolver implements
	ViewResolver, InitializingBean {

    private static final UrlPathHelper urlPathHelper = new UrlPathHelper();

    protected String fileExtension = "pdf";
    protected String templateExtension = "_" + fileExtension;

    static {
	urlPathHelper.setUrlDecode(false);
    }

    private UrlBasedViewResolver templateViewResolver;

    public SmartViewResolver() {
	setViewClass(requiredViewClass());
	setContentType("application/pdf");
    }

    @Override
    protected Class requiredViewClass() {
	return DownloadView.class;
    }

    /**
     * @return the templateViewResolver
     */
    protected UrlBasedViewResolver getTemplateViewResolver() {
	return templateViewResolver;
    }

    /**
     * @param templateViewResolver
     *            the templateViewResolver to set
     */
    public void setTemplateViewResolver(
	    UrlBasedViewResolver templateViewResolver) {
	this.templateViewResolver = templateViewResolver;
    }

    @Override
    public View resolveViewName(String viewName, Locale locale)
	    throws Exception {
	View view = null;
	if (canHandleContentType()) {
	    String viewNameWithExtension = viewName + templateExtension;
	    try {
		view = getTemplateViewResolver().resolveViewName(
			viewNameWithExtension, locale);
	    } catch (Exception e1) {
		if (logger.isDebugEnabled()) {
		    logger.debug("Isn't [" + viewNameWithExtension
			    + "] a valid template? " + e1);
		}
	    }
	    if (view == null) {
		view = super.resolveViewName(viewNameWithExtension, locale);
	    }
	}
	return view;
    }

    protected boolean canHandleContentType() {
	boolean isPdf = false;
	RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
	Assert.isInstanceOf(ServletRequestAttributes.class, attrs);
	HttpServletRequest request = ((ServletRequestAttributes) attrs)
		.getRequest();
	String requestUri = urlPathHelper.getLookupPathForRequest(request);
	String filename = WebUtils.extractFullFilenameFromUrlPath(requestUri);
	String extension = StringUtils.getFilenameExtension(filename);

	if (StringUtils.hasText(extension)) {
	    isPdf = fileExtension.equals(extension.toLowerCase(Locale.ENGLISH));
	}
	return isPdf;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
	templateViewResolver.setPrefix(getPrefix());
	templateViewResolver.setSuffix(getSuffix());
	templateViewResolver.setViewNames(getViewNames());
    }
}
