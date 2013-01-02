smartview-spring-webmvc
=======================

Simple resumible download view with a smart view resolver to improve the file generation and streaming with spring webmvc 3.1

The DownloadView is based on [this](http://balusc.blogspot.com.es/2009/02/fileservlet-supporting-resume-and.html) post from BalusC

Maven configuration
===================

		<!-- Smart view -->
		<dependency>
			<groupId>com.github.kpacha</groupId>
			<artifactId>smartview-spring-webmvc</artifactId>
			<version>${smartview-spring-webmvc.version}</version>
		</dependency>


Servlet context configuration
=============================

With Tiles2 and the [tiles2.pdf-spring-webmvc](https://github.com/kpacha/tiles2.pdf-spring-webmvc) library
	
	<bean id="tilesPDFViewResolver"
		class="org.springframework.web.servlet.view.smart.SmartViewResolver">
		<property name="order" value="1" />
		<property name="viewNames">
			<list>
				<value>*_pdf</value>
			</list>
		</property>
		<property name="templateViewResolver">
			<bean class="org.springframework.web.servlet.view.tiles2.TilesPDFViewResolver"/>
		</property>
	</bean>
	
	<bean
		class="org.springframework.web.servlet.view.ContentNegotiatingViewResolver">
		<property name="order" value="2" />
		<property name="mediaTypes">
			<map>
				<entry key="json" value="application/json" />
				<entry key="mp4" value="video/mp4" />
				<entry key="ogg" value="audio/ogg" />
			</map>
		</property>

		<property name="defaultViews">
			<list>
				<!-- JSON View -->
				<bean
					class="org.springframework.web.servlet.view.json.MappingJacksonJsonView">
				</bean>
				<!-- MP4 View -->
				<bean id="mp4Downloader" class="org.springframework.web.servlet.view.smart.DownloadView">
					<property name="contentType" value="video/mp4" />
					<property name="url" value="video/mp4" />
				</bean>
				<!-- OGG View -->
				<bean id="oggDownloader" class="org.springframework.web.servlet.view.smart.DownloadView">
					<property name="contentType" value="audio/ogg" />
					<property name="url" value="audio/ogg" />
				</bean>
			</list>
		</property>
		<property name="ignoreAcceptHeader" value="true" />
	</bean>
	
	<bean id="tilesViewResolver"
		class="org.springframework.web.servlet.view.tiles2.TilesViewResolver">
		<property name="order" value="3" />
	</bean>

With [mustache.java](https://github.com/spullara/mustache.java) and the [mustache.java-spring-webmvc](https://github.com/kpacha/mustache.java-spring-webmvc) library


	<bean id="mustachePDFViewResolver" class="org.springframework.web.servlet.view.smart.SmartViewResolver">
		<property name="order" value="1" />
		<property name="prefix" value="/WEB-INF/mustache/" />
		<property name="suffix" value=".mustache" />
		<property name="viewNames">
			<list>
				<value>*_pdf</value>
			</list>
		</property>
		<property name="templateViewResolver">
			<bean class="com.github.kpacha.springmvc.view.SmartPDFViewResolver">
				<property name="templateLoader" ref="mustacheTemplateLoader" />
				<property name="excludedViewNames">
					<list>
						<value>*.json*</value>
						<value>*.ogg*</value>
						<value>*.mp4*</value>
					</list>
				</property>
			</bean>
		</property>
	</bean>

	<bean
		class="org.springframework.web.servlet.view.ContentNegotiatingViewResolver">
		<property name="order" value="2" />
		<property name="mediaTypes">
			<map>
				<entry key="json" value="application/json" />
				<entry key="mp4" value="video/mp4" />
				<entry key="ogg" value="audio/ogg" />
			</map>
		</property>

		<property name="defaultViews">
			<list>
				<!-- JSON View -->
				<bean
					class="org.springframework.web.servlet.view.json.MappingJacksonJsonView">
				</bean>
				<!-- MP4 View -->
				<bean id="mp4Downloader" class="org.springframework.web.servlet.view.smart.DownloadView">
					<property name="contentType" value="video/mp4" />
					<property name="url" value="video/mp4" />
				</bean>
				<!-- OGG View -->
				<bean id="oggDownloader" class="org.springframework.web.servlet.view.smart.DownloadView">
					<property name="contentType" value="audio/ogg" />
					<property name="url" value="audio/ogg" />
				</bean>
			</list>
		</property>
		<property name="ignoreAcceptHeader" value="true" />
	</bean>

	<bean id="mustacheViewResolver"
		class="org.springframework.web.servlet.view.mustache.MustacheViewResolver">
		<property name="order" value="3" />
		<property name="prefix" value="/WEB-INF/mustache/" />
		<property name="suffix" value=".mustache" />
		<property name="templateLoader" ref="mustacheTemplateLoader" />
		<property name="excludedViewNames">
			<list>
				<value>*.json</value>
				<value>*.pdf</value>
				<value>*.ogg</value>
				<value>*.mp4</value>
			</list>
		</property>
	</bean>
