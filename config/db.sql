use dns_sys;

CREATE TABLE `SysConfig` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
   `User_Name` varchar(255) NOT NULL,
  `User_Passwd` varchar(255) NOT NULL,
  /* 可以是多个父地址 ，逗号分隔*/
  `Parent_DNS` varchar(255) NOT NULL,
  /*所有情况均未命中的默认跳转地址*/
  `Default_IP` varchar(255) NOT NULL,
  /*正常解析的存活时间*/
  `Alive_N` int(11) NOT NULL,
  /*非正常解析的存活时间*/
  `Alive_T` int(11) NOT NULL,
  /*每年自动重新统计时间,若未0，则不重置*/
  `Reset_Time` varchar(255) NOT NULL,
  /*每天发送统计邮件的时间，若未0则不发送*/
  `Mail_Time` varchar(255) NOT NULL,
  /*发送统计邮件的地址，可多个，逗号隔开*/
  `Mail_Addr` varchar(255) NOT NULL,
  /*是否开启查询功能*/
  `Enable`	bit(1) NOT NULL default 0,
  `Date` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;
/*DNS记录*/
CREATE TABLE `DNS_Pool` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
   `Host` varchar(255) NOT NULL,
  `QueryType` varchar(12) NOT NULL,
  `Value` varchar(255) NOT NULL,
   `Hit`  int(11) default 0,
  `Date` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;
/*查询记录*/
CREATE TABLE `Record` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
   `Host` varchar(255) NOT NULL,
  `HostName` varchar(255) NOT NULL,
  `MAC` varchar(32) NOT NULL,
    `Type` varchar(12) NOT NULL,
  `IP` varchar(15) NOT NULL,
  `Date` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;
/*黑名单*/
CREATE TABLE `BlackList` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `IP` varchar(255) NOT NULL,
  `Enable` bit(1) NOT NULL,
  `Date` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;

/*自定义规则*/
CREATE TABLE `Rules` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  /*要查询的域名*/
  `Host` varchar(255) NOT NULL,
  /*跳转的目标地址*/
  `IP` varchar(32) NOT NULL,
  /*拦截的来访地址，若0.0.0.0则拦截所有*/
  `TargetIP` varchar(32) default '0.0.0.0',
  `Enable` bit(1) default true,
  `Date` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;

CREATE TABLE `Hit` (
  `HitID` int(11) NOT NULL AUTO_INCREMENT,
  `RuleID` int(11) NOT NULL,
  `Host` varchar(255) NOT NULL,
  `HostName` varchar(255) NOT NULL,
  `MAC` 	varchar(255) NOT NULL,
  `Date` varchar(32) NOT NULL,
  PRIMARY KEY (`HitID`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;