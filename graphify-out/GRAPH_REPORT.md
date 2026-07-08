# Graph Report - .  (2026-07-08)

## Corpus Check
- Corpus is ~12,174 words - fits in a single context window. You may not need a graph.

## Summary
- 194 nodes · 287 edges · 19 communities (9 shown, 10 thin omitted)
- Extraction: 61% EXTRACTED · 39% INFERRED · 0% AMBIGUOUS · INFERRED: 112 edges (avg confidence: 0.82)
- Token cost: 12,000 input · 6,000 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Risk Project Data Model|Risk Project Data Model]]
- [[_COMMUNITY_User Identity Model|User Identity Model]]
- [[_COMMUNITY_Web Controllers and Routes|Web Controllers and Routes]]
- [[_COMMUNITY_Risk Mitigation Actions|Risk Mitigation Actions]]
- [[_COMMUNITY_View Templates and Initialization|View Templates and Initialization]]
- [[_COMMUNITY_Web Application Configuration|Web Application Configuration]]
- [[_COMMUNITY_User Authentication and Service|User Authentication and Service]]
- [[_COMMUNITY_Corporate Identity Symbols|Corporate Identity Symbols]]
- [[_COMMUNITY_Risk Calculation Service|Risk Calculation Service]]
- [[_COMMUNITY_Application Verification Tests|Application Verification Tests]]
- [[_COMMUNITY_Spring Boot Initialization|Spring Boot Initialization]]
- [[_COMMUNITY_Risk Mitigation Service|Risk Mitigation Service]]
- [[_COMMUNITY_Automation Shell Scripts|Automation Shell Scripts]]
- [[_COMMUNITY_Mitigation Data Access|Mitigation Data Access]]
- [[_COMMUNITY_Static Resource Mapping|Static Resource Mapping]]

## God Nodes (most connected - your core abstractions)
1. `RiskProject` - 53 edges
2. `WebController` - 18 edges
3. `User` - 18 edges
4. `PengendalianRisiko` - 16 edges
5. `WebController` - 9 edges
6. `Pusri Logo Image` - 9 edges
7. `UserService` - 8 edges
8. `RiskProject` - 7 edges
9. `RiskProjectRepository` - 6 edges
10. `Uploaded Profile Picture (Pusri Logo 2)` - 6 edges

## Surprising Connections (you probably didn't know these)
- `Record Tool Use PowerShell Script` --semantically_similar_to--> `Record Tool Use Shell Script`  [INFERRED] [semantically similar]
  .github/modernize/java-upgrade/hooks/scripts/recordToolUse.ps1 → .github/modernize/java-upgrade/hooks/scripts/recordToolUse.sh
- `Uploaded Profile Picture (Pusri Logo)` --references--> `PT Pupuk Sriwidjaja Palembang (PUSRI)`  [EXTRACTED]
  uploads/52141ef4-ace3-41e7-b14a-8a958e417072_pusri-2.webp → src/main/resources/static/img/logo.webp
- `Uploaded Profile Picture (Pusri Logo 2)` --references--> `PT Pupuk Sriwidjaja Palembang (PUSRI)`  [EXTRACTED]
  uploads/6e9e5976-4913-4647-acf7-63104b8612af_pusri-2.webp → src/main/resources/static/img/logo.webp
- `DataInitializer` --references--> `User`  [EXTRACTED]
  src/main/java/com/pusri/risk/DataInitializer.java → src/main/java/com/pusri/risk/model/User.java
- `RiskProjectService` --calls--> `RiskProject`  [EXTRACTED]
  src/main/java/com/pusri/risk/service/RiskProjectService.java → src/main/java/com/pusri/risk/model/RiskProject.java

## Hyperedges (group relationships)
- **Risk Management Core Domain Models** — riskproject_riskproject, pengendalianrisiko_pengendalianrisiko, user_user [INFERRED 0.95]
- **Data Access Repository Layer** — userrepository_userrepository, riskprojectrepository_riskprojectrepository, pengendalianrisikorepository_pengendalianrisikorepository [INFERRED 0.95]
- **Business Logic Service Layer** — userservice_userservice, riskprojectservice_riskprojectservice, pengendalianrisikoservice_pengendalianrisikoservice [INFERRED 0.95]
- **Pusri Logo Components** — pusri_2_logo_image, pusri_2_kajang_boat, pusri_2_paddy_cotton, pusri_2_urea_granules, pusri_2_pusri_company [EXTRACTED 1.00]

## Communities (19 total, 10 thin omitted)

### Community 1 - "User Identity Model"
Cohesion: 0.12
Nodes (4): CommandLineRunner, User, DataInitializer, RiskmanagerApplication

### Community 4 - "View Templates and Initialization"
Cohesion: 0.22
Nodes (11): DataInitializer, PengendalianRisiko, PengendalianRisikoRepository, PengendalianRisikoService, RiskProject, RiskProjectRepository, RiskProjectService, User (+3 more)

### Community 6 - "Web Application Configuration"
Cohesion: 0.21
Nodes (9): WebConfig, Logo WebP Image, PT Pupuk Sriwidjaja Palembang (PUSRI), Kajang Boat Symbol, Paddy and Cotton Symbol, Urea Crystals Symbol, Uploaded Profile Picture (Pusri Logo 2), Uploaded Profile Picture (Pusri Logo) (+1 more)

### Community 8 - "Corporate Identity Symbols"
Cohesion: 0.29
Nodes (10): Kajang Boat Symbol, Palembang City Location, Fertilizer Atom/Granule Logo Element, Pusri Logo Image, Kajang Boat Logo Element, Rice and Cotton Logo Element, PT Pupuk Sriwidjaja Palembang (PUSRI), Paddy and Cotton Symbol (+2 more)

### Community 12 - "Spring Boot Initialization"
Cohesion: 0.67
Nodes (3): Maven Wrapper Script, RiskmanagerApplication, RiskmanagerApplicationTests

## Knowledge Gaps
- **6 isolated node(s):** `PengendalianRisikoRepository`, `Record Tool Use PowerShell Script`, `Record Tool Use Shell Script`, `WebConfig`, `RiskmanagerApplicationTests` (+1 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **10 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `RiskProject` connect `Risk Project Data Model` to `Risk Calculation Service`, `Risk Matrix Adjustments`, `Web Controllers and Routes`, `Risk Project Administration`?**
  _High betweenness centrality (0.209) - this node is a cross-community bridge._
- **Why does `WebController` connect `Web Controllers and Routes` to `User Identity Model`, `Risk Project Administration`, `Web Application Configuration`, `User Authentication and Service`, `Risk Matrix Adjustments`?**
  _High betweenness centrality (0.162) - this node is a cross-community bridge._
- **Why does `User` connect `User Identity Model` to `Web Controllers and Routes`, `Risk Project Administration`, `User Authentication and Service`?**
  _High betweenness centrality (0.085) - this node is a cross-community bridge._
- **Are the 5 inferred relationships involving `WebController` (e.g. with `login.html` and `dashboard.html`) actually correct?**
  _`WebController` has 5 INFERRED edges - model-reasoned connections that need verification._
- **What connects `PengendalianRisikoRepository`, `Record Tool Use PowerShell Script`, `Record Tool Use Shell Script` to the rest of the system?**
  _12 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Risk Project Data Model` be split into smaller, more focused modules?**
  _Cohesion score 0.06666666666666667 - nodes in this community are weakly interconnected._
- **Should `User Identity Model` be split into smaller, more focused modules?**
  _Cohesion score 0.11666666666666667 - nodes in this community are weakly interconnected._