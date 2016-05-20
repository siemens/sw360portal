/*
 * Copyright (c) Bosch Software Innovations GmbH 2016.
 * Part of the SW360 Portal Project.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License Version 2.0 as published by the
 * Free Software Foundation with classpath exception.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License version 2.0 for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (please see the COPYING file); if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */
package com.bosch.osmi.sw360.cvesearch.datasource.json;

import com.bosch.osmi.sw360.cvesearch.datasource.CveSearchData;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class CveSearchJsonParserTest {

    String SEARCH_RESULT_EMPTY = "";
    String SEARCH_RESULT_EMPTYLIST = "[]";
    String SEARCH_RESULT_EMPTYOBJ = "{}";
    String SEARCH_RESULT_SINGLE_A = "{\"id\": \"CVE-2002-0438\", \"summary\": \"ZyXEL ZyWALL 10 before 3.50 allows remote attackers to cause a denial of service via an ARP packet with the firewall's IP address and an incorrect MAC address, which causes the firewall to disable the LAN interface.\", \"impact\": {\"confidentiality\": \"NONE\", \"availability\": \"PARTIAL\", \"integrity\": \"NONE\"}, \"vulnerable_configuration_cpe_2_2\": [\"cpe:/h:zyxel:zywall10:3.20_wa0\", \"cpe:/h:zyxel:zywall10:3.20_wa1\", \"cpe:/h:zyxel:zywall10:3.24_wa0\", \"cpe:/h:zyxel:zywall10:3.24_wa1\", \"cpe:/h:zyxel:zywall10:3.24_wa2\", \"cpe:/h:zyxel:zywall10:3.50_wa1\"], \"cvss\": 5.0, \"vulnerable_configuration\": [\"cpe:2.3:h:zyxel:zywall10:3.20_wa0\", \"cpe:2.3:h:zyxel:zywall10:3.20_wa1\", \"cpe:2.3:h:zyxel:zywall10:3.24_wa0\", \"cpe:2.3:h:zyxel:zywall10:3.24_wa1\", \"cpe:2.3:h:zyxel:zywall10:3.24_wa2\", \"cpe:2.3:h:zyxel:zywall10:3.50_wa1\"], \"cvss-time\": \"2004-01-01T00:00:00.000-05:00\", \"access\": {\"vector\": \"NETWORK\", \"complexity\": \"LOW\", \"authentication\": \"NONE\"}, \"Published\": \"2002-07-26T00:00:00.000-04:00\", \"references\": [\"http://www.securityfocus.com/bid/4272\", \"http://www.securityfocus.com/archive/1/261411\", \"http://www.iss.net/security_center/static/8436.php\", \"http://archives.neohapsis.com/archives/vulnwatch/2002-q1/0067.html\", \"ftp://ftp.zyxel.com/public/zywall10/firmware/zywall10_V3.50%28WA.2%29C0_Standard.zip\"], \"Modified\": \"2008-09-10T15:12:03.853-04:00\"}";

// SEARCH_RESULT_SINGLE_B:
//{
//  "vulnerable_configuration_cpe_2_2": [
//    "cpe:/h:zyxel:zywall10:3.20_wa0",
//    "cpe:/h:zyxel:zywall10:3.20_wa1",
//    "cpe:/h:zyxel:zywall10:3.24_wa0",
//    "cpe:/h:zyxel:zywall10:3.24_wa1",
//    "cpe:/h:zyxel:zywall10:3.24_wa2",
//    "cpe:/h:zyxel:zywall10:3.50_wa1"
//  ],
//  "vulnerable_configuration": [
//    {
//      "title": "Zyxel Zywall10 3.20 Wa0",
//      "id": "cpe:2.3:h:zyxel:zywall10:3.20_wa0"
//    },
//    {
//      "title": "Zyxel Zywall10 3.20 Wa1",
//      "id": "cpe:2.3:h:zyxel:zywall10:3.20_wa1"
//    },
//    {
//      "title": "Zyxel Zywall10 3.24 Wa0",
//      "id": "cpe:2.3:h:zyxel:zywall10:3.24_wa0"
//    },
//    {
//      "title": "Zyxel Zywall10 3.24 Wa1",
//      "id": "cpe:2.3:h:zyxel:zywall10:3.24_wa1"
//    },
//    {
//      "title": "Zyxel Zywall10 3.24 Wa2",
//      "id": "cpe:2.3:h:zyxel:zywall10:3.24_wa2"
//    },
//    {
//      "title": "Zyxel Zywall10 3.50 Wa1",
//      "id": "cpe:2.3:h:zyxel:zywall10:3.50_wa1"
//    }
//  ],
//  "summary": "ZyXEL ZyWALL 10 before 3.50 allows remote attackers to cause a denial of service via an ARP packet with the firewall's IP address and an incorrect MAC address, which causes the firewall to disable the LAN interface.",
//  "references": [
//    "http://www.securityfocus.com/bid/4272",
//    "http://www.securityfocus.com/archive/1/261411",
//    "http://www.iss.net/security_center/static/8436.php",
//    "http://archives.neohapsis.com/archives/vulnwatch/2002-q1/0067.html",
//    "ftp://ftp.zyxel.com/public/zywall10/firmware/zywall10_V3.50%28WA.2%29C0_Standard.zip"
//  ],
//  "ranking": [
//    [
//      {
//        "circl": 2
//      }
//    ]
//  ],
//  "map_cve_scip": {
//    "sciplink": "http://www.scip.ch/en/?vuldb.18458",
//    "scipid": "18458"
//  },
//  "map_cve_bid": {
//    "bidid": "4272"
//  },
//  "impact": {
//    "integrity": "NONE",
//    "confidentiality": "NONE",
//    "availability": "PARTIAL"
//  },
//  "id": "CVE-2002-0438",
//  "cvss-time": "2004-01-01T00:00:00.000-05:00",
//  "cvss": 5.0,
//  "access": {
//    "vector": "NETWORK",
//    "complexity": "LOW",
//    "authentication": "NONE"
//  },
//  "Published": "2002-07-26T00:00:00.000-04:00",
//  "Modified": "2008-09-10T15:12:03.853-04:00"
//}
     String SEARCH_RESULT_SINGLE_B = "{\"vulnerable_configuration_cpe_2_2\": [\"cpe:/h:zyxel:zywall10:3.20_wa0\", \"cpe:/h:zyxel:zywall10:3.20_wa1\", \"cpe:/h:zyxel:zywall10:3.24_wa0\", \"cpe:/h:zyxel:zywall10:3.24_wa1\", \"cpe:/h:zyxel:zywall10:3.24_wa2\", \"cpe:/h:zyxel:zywall10:3.50_wa1\"], \"vulnerable_configuration\": [{\"title\": \"Zyxel Zywall10 3.20 Wa0\", \"id\": \"cpe:2.3:h:zyxel:zywall10:3.20_wa0\"}, {\"title\": \"Zyxel Zywall10 3.20 Wa1\", \"id\": \"cpe:2.3:h:zyxel:zywall10:3.20_wa1\"}, {\"title\": \"Zyxel Zywall10 3.24 Wa0\", \"id\": \"cpe:2.3:h:zyxel:zywall10:3.24_wa0\"}, {\"title\": \"Zyxel Zywall10 3.24 Wa1\", \"id\": \"cpe:2.3:h:zyxel:zywall10:3.24_wa1\"}, {\"title\": \"Zyxel Zywall10 3.24 Wa2\", \"id\": \"cpe:2.3:h:zyxel:zywall10:3.24_wa2\"}, {\"title\": \"Zyxel Zywall10 3.50 Wa1\", \"id\": \"cpe:2.3:h:zyxel:zywall10:3.50_wa1\"}], \"summary\": \"ZyXEL ZyWALL 10 before 3.50 allows remote attackers to cause a denial of service via an ARP packet with the firewall's IP address and an incorrect MAC address, which causes the firewall to disable the LAN interface.\", \"references\": [\"http://www.securityfocus.com/bid/4272\", \"http://www.securityfocus.com/archive/1/261411\", \"http://www.iss.net/security_center/static/8436.php\", \"http://archives.neohapsis.com/archives/vulnwatch/2002-q1/0067.html\", \"ftp://ftp.zyxel.com/public/zywall10/firmware/zywall10_V3.50%28WA.2%29C0_Standard.zip\"], \"ranking\": [[{\"circl\": 2}]], \"map_cve_scip\": {\"sciplink\": \"http://www.scip.ch/en/?vuldb.18458\", \"scipid\": \"18458\"}, \"map_cve_bid\": {\"bidid\": \"4272\"}, \"impact\": {\"integrity\": \"NONE\", \"confidentiality\": \"NONE\", \"availability\": \"PARTIAL\"}, \"id\": \"CVE-2002-0438\", \"cvss-time\": \"2004-01-01T00:00:00.000-05:00\", \"cvss\": 5.0, \"access\": {\"vector\": \"NETWORK\", \"complexity\": \"LOW\", \"authentication\": \"NONE\"}, \"Published\": \"2002-07-26T00:00:00.000-04:00\", \"Modified\": \"2008-09-10T15:12:03.853-04:00\"}";

    String SEARCH_RESULT_FULL = "[" + SEARCH_RESULT_SINGLE_A + ",  " + SEARCH_RESULT_SINGLE_B +
            ", {\"id\": \"CVE-2004-1789\", \"summary\": \"Cross-site scripting (XSS) vulnerability in the web management interface in ZyWALL 10 4.07 allows remote attackers to inject arbitrary web script or HTML via the rpAuth_1 page.\", \"impact\": {\"confidentiality\": \"NONE\", \"availability\": \"NONE\", \"integrity\": \"PARTIAL\"}, \"vulnerable_configuration_cpe_2_2\": [\"cpe:/h:zyxel:zywall10:3.20_wa0\", \"cpe:/h:zyxel:zywall10:3.20_wa1\", \"cpe:/h:zyxel:zywall10:3.24_wa0\", \"cpe:/h:zyxel:zywall10:3.24_wa1\", \"cpe:/h:zyxel:zywall10:3.24_wa2\", \"cpe:/h:zyxel:zywall10:3.50_wa1\", \"cpe:/h:zyxel:zywall10:3.50_wa2\", \"cpe:/h:zyxel:zywall10:4.07\"], \"cvss\": 4.3, \"vulnerable_configuration\": [\"cpe:2.3:h:zyxel:zywall10:3.20_wa0\", \"cpe:2.3:h:zyxel:zywall10:3.20_wa1\", \"cpe:2.3:h:zyxel:zywall10:3.24_wa0\", \"cpe:2.3:h:zyxel:zywall10:3.24_wa1\", \"cpe:2.3:h:zyxel:zywall10:3.24_wa2\", \"cpe:2.3:h:zyxel:zywall10:3.50_wa1\", \"cpe:2.3:h:zyxel:zywall10:3.50_wa2\", \"cpe:2.3:h:zyxel:zywall10:4.07\"], \"cvss-time\": \"2005-05-30T18:43:00.000-04:00\", \"access\": {\"vector\": \"NETWORK\", \"complexity\": \"MEDIUM\", \"authentication\": \"NONE\"}, \"Published\": \"2004-12-31T00:00:00.000-05:00\", \"references\": [\"http://xforce.iss.net/xforce/xfdb/14163\", \"http://www.securityfocus.com/bid/9373\", \"http://www.securityfocus.com/archive/1/349085\", \"http://www.osvdb.org/3443\", \"http://securitytracker.com/id?1008644\", \"http://www.osvdb.org/12793\"], \"Modified\": \"2008-09-05T16:42:21.663-04:00\"}]";

    CveSearchJsonParser cveSearchJsonParserSingle;
    CveSearchJsonParser cveSearchJsonParserList;

    @Before
    public void setUp() {
        Type listTargetType = new TypeToken<List<CveSearchData>>(){}.getType();
        Type singleTargetType = new TypeToken<CveSearchData>(){}.getType();

        cveSearchJsonParserSingle = new CveSearchJsonParser(singleTargetType);
        cveSearchJsonParserList = new CveSearchJsonParser(listTargetType);
    }

    private BufferedReader toBufferedReader(String in) {
        return new BufferedReader(new StringReader(in));
    }

    @Test
    public void singleEmpty() {
        Object resultO = cveSearchJsonParserSingle.parseJsonBuffer(toBufferedReader(SEARCH_RESULT_EMPTY));
        CveSearchData result = (CveSearchData) resultO;
    }

    @Test
    public void singleEmptyObj() {
        Object resultO = cveSearchJsonParserSingle.parseJsonBuffer(toBufferedReader(SEARCH_RESULT_EMPTYOBJ));
        CveSearchData result = (CveSearchData) resultO;
    }

    @Test
    public void singleFull_A() {
        Object resultO = cveSearchJsonParserSingle.parseJsonBuffer(toBufferedReader(SEARCH_RESULT_SINGLE_A));
        CveSearchData result = (CveSearchData) resultO;
        assert(result.getCvss() == 5.0);
        assert("CVE-2002-0438".equals(result.getId()));

        Map<String, String> vce = result.getVulnerable_configuration().stream()
                .findAny()
                .get();
        assert(vce.get("id").length() > 2);
    }

    @Test
    public void singleFull_B() {
        Object resultO = cveSearchJsonParserSingle.parseJsonBuffer(toBufferedReader(SEARCH_RESULT_SINGLE_B));
        CveSearchData result = (CveSearchData) resultO;
        assert(result.getCvss() == 5.0);
        assert("CVE-2002-0438".equals(result.getId()));
        assert(result.getVulnerable_configuration().size() == 6);

        Map<String, String> vce = result.getVulnerable_configuration().stream()
                .findAny()
                .get();
        assert(vce.get("id").length() > 2);
        assert(vce.get("title").length() > 2);
        assert(! vce.get("id").equals(vce.get("title")));
    }

    @Test
    public void listEmpty() {
        Object resultO = cveSearchJsonParserList.parseJsonBuffer(toBufferedReader(SEARCH_RESULT_EMPTY));
        List<CveSearchData> result = (List<CveSearchData>) resultO;
        assert(result==null);
    }

    @Test
    public void listEmptyList() {
        Object resultO = cveSearchJsonParserList.parseJsonBuffer(toBufferedReader(SEARCH_RESULT_EMPTYLIST));
        List<CveSearchData> result = (List<CveSearchData>) resultO;
        assert(result.size()==0);
    }

    @Test
    public void listSingletonList() {
        Object resultO = cveSearchJsonParserList.parseJsonBuffer(toBufferedReader("[" + SEARCH_RESULT_SINGLE_A + "]"));
        List<CveSearchData> result = (List<CveSearchData>) resultO;
        assert(result.size()==1);
    }

    @Test
    public void listFullList() {
        Object resultO = cveSearchJsonParserList.parseJsonBuffer(toBufferedReader(SEARCH_RESULT_FULL));
        List<CveSearchData> result = (List<CveSearchData>) resultO;
        assert(result.size()==3);
    }
}