filesToMoveMap = {
	"/org/alice/ide/video/croquet.properties" : "/org/alice/ide/youtube/croquet/croquet.properties",
	"/org/alice/ide/croquet/models/project/croquet.properties" : "/org/alice/ide/croquet/models/project/stats/croquet/croquet.properties"
}

filesToDelete = [
	"/org/lgna/cheshire/task/stencil/croquet.properties",
	"/org/alice/stageide/sceneeditor/snap/snap.properties"
]

keysToDelete = {
	"/org/alice/ide/croquet/models/ast/croquet.properties" : [
		"DeleteFieldFrameComposite",
		"DeleteFieldFrameComposite.because",
		"DeleteFieldFrameComposite.ifYouWantToDelete",
		"DeleteFieldFrameComposite.mustRemove",
		"DeleteFieldFrameComposite.pluralAccessReference",
		"DeleteFieldFrameComposite.pluralTheseReferences",
		"DeleteFieldFrameComposite.singularAccessReference",
		"DeleteFieldFrameComposite.singularThisReference",
		"DeleteFieldFrameComposite.unableToDelete",
	],
	"/org/alice/ide/croquet/models/project/stats/croquet/croquet.properties" : [
		"FieldSearchTabCompsoite",
        "FindFieldsFrameComposite",
        "FindMethodsFrameComposite",
        "FindReferencesFrameComposite",
        "MethodReferencesComposite",
        "MethodSearchComposite",
        "MethodSearchComposite.showFunctions",
        "MethodSearchComposite.showGenerated",
        "MethodSearchComposite.showProcedure",
        "MethodSearchTabComposite",
	],

	"/org/alice/ide/croquet/models/projecturi/croquet.properties" : [
		"ClearanceCheckingExitOperation",
	],

	"/org/alice/ide/youtube/croquet/croquet.properties" : [
		"IsRecordingState",
		"ProofOfConceptRecordVideoOperation",
	],

	"/org/alice/stageide/personresource/croquet.properties" : [
		"BodyTabComposite",
        "BodyTabComposite.obesityLevelState(0.0)",
        "BodyTabComposite.obesityLevelState(1.0)",
        "HeadTabComposite",
        "RandomPersonResourceComposite",
	],

	"/org/alice/ide/croquet/models/ui/preferences/croquet.properties" : [
		"IsExposingReassignableStatusState",
		"IsIncludingConstructors",
		"IsIncludingProgramType",
		"IsIncludingThisForFieldAccessesState",
	],

	"/org/lgna/story/resources/GalleryTags.properties" : [
		"dining_room",
		"fishing_boat",
		"look"
	],
}

keysMap = {
	"/org/alice/ide/croquet/models/ui/locale/croquet.properties" : {
		"LocaleSelectionState" : "LocaleState"
	},
	"/org/alice/ide/croquet/models/help/croquet.properties" : {
		"ReportBugComposite" : "ReportIssueComposite.reportBug",
		"RequestNewFeatureComposite" : "ReportIssueComposite.requestNewFeature",
		"SuggestImprovementComposite" : "ReportIssueComposite.suggestImprovement",
		"BugLoginComposite.commitOperation" : "AbstractLoginComposite.commitOperation",
		"BugLoginComposite.displayPasswordState" : "AbstractLoginComposite.displayPasswordState",
		"BugLoginComposite.passwordState.sidekickLabel" : "AbstractLoginComposite.passwordState.sidekickLabel",
		"BugLoginComposite.userNameState.sidekickLabel" : "AbstractLoginComposite.userNameState.sidekickLabel",
	},

	"/org/lgna/story/resources/GalleryNames.properties" : {
		"org.lgna.story.resources.biped.BabyYeti" : "org.lgna.story.resources.biped.YetiBaby",
		"org.lgna.story.resources.marinemammal.BabyWalrus" : "org.lgna.story.resources.marinemammal.WalrusBaby",
		"org.lgna.story.resources.quadruped.BabyDragon" : "org.lgna.story.resources.quadruped.DragonBaby",
		"org.lgna.story.resources.prop.FishingBoat" : "org.lgna.story.resources.watercraft.FishingBoat",
		"org.lgna.story.resources.prop.Helicopter" : "org.lgna.story.resources.aircraft.Helicopter",
		"org.lgna.story.resources.prop.PirateShip" : "org.lgna.story.resources.watercraft.PirateShip",
		"org.lgna.story.resources.prop.Submarine" : "org.lgna.story.resources.watercraft.Submarine",
		"org.lgna.story.resources.prop.UFO" : "org.lgna.story.resources.aircraft.UFO",
	},

	"/org/alice/stageide/personresource/croquet.properties" : {
		"HairColorNameState.sidekickLabel" : "HairTabComposite.hairColorNameState.sidekickLabel",
		"HeadTabComposite.baseEyeColorState.sidekickLabel" : "FaceTabComposite.baseEyeColorState.sidekickLabel"
	},

	"/org/alice/ide/croquet/models/ui/formatter/croquet.properties" : {
		"FormatterSelectionState" : "FormatterState",
	},
}

keysToMove = [
	(
		"/org/alice/stageide/gallerybrowser/croquet.properties",
		"/org/alice/stageide/gallerybrowser/search/croquet/croquet.properties",
		[
			"SearchTab",
			"SearchTab.filterState.sidekickLabel",
			"SearchTab.filterState.textForBlankCondition",
			"SearchTab.noEntryLabel",
			"SearchTab.noMatchesLabel"
		]
	)
]
