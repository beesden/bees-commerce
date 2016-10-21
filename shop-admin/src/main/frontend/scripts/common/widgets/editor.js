(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' )
		.directive( 'editor', function( $q, $timeout, assetService ) {

			return {

				require: 'ngModel',
				scope: {
					config: '<editorConfig'
				},

				link: function( scope, element, attrs, ngModel ) {

					var config = scope.config || {};
					element.attr( 'readonly', 'readonly' );

					switch ( attrs.editor ) {
						case 'ftl':
						case 'html':
						case 'javascript':
							assetService.loadScript( 'https://cdnjs.cloudflare.com/ajax/libs/ace/1.2.3/ace.js' )
								.then( function() {
									$q.all( [
										assetService.loadScript( 'https://cdnjs.cloudflare.com/ajax/libs/ace/1.2.3/ext-language_tools.js' )
									] ).then( function() {
										var editor = ng.element( '<div>' );
										element.after( editor ).css( 'display', 'none' );
										element.removeAttr( 'readonly' );
										editor = ace.edit( editor[ 0 ] );

										editor.setTheme( 'ace/theme/idle_fingers' );
										editor.session.setMode( 'ace/mode/' + attrs.editor );
										editor.$blockScrolling = Infinity;
										editor.setOptions( {
											maxLines: 40,
											minLines: 10,
											enableBasicAutocompletion: true,
											enableLiveAutocompletion: false
										} );
										// Update model on change
										editor.getSession().on( 'change', function() {
											scope.$evalAsync( function() {
												ngModel.$setViewValue( editor.getValue() );
											} );
										} );

										ngModel.$formatters.push( function( value ) {
											return value;
										} );

										ngModel.$render = function() {
											if ( ngModel.$viewValue ) {
												editor.setValue( ngModel.$viewValue, -1 );
											}
										};

										// Set initial value
										if ( ngModel.$viewValue ) {
											editor.setValue( ngModel.$viewValue, -1 );
										}
									} );
								} );
							break;
						case 'content':
						case 'simple':
						case 'tinymce':
							assetService.loadScript( 'https://cdn.tinymce.com/4/tinymce.js' ).then( function() {
								var tinyInstance;
								element.removeAttr( 'readonly' );
								if ( !attrs.id ) {
									attrs.$set( 'id', 'tinymce-' + tinymce.editors.length );
								}

								ngModel.$render = function() {
									if ( !tinyInstance ) {
										tinyInstance = tinymce.get( attrs.id );
									}
									if ( tinyInstance.getDoc() && ngModel.$modelValue ) {
										tinyInstance.setContent( ngModel.$modelValue );
									}
								};

								tinymce.init( {
									body_class: 'editor',
									content_css: '/resources/styles/editor.css',
									selector: '#' + attrs.id,
									height: config.height || 200,
									plugins: 'paste code link lists noneditable table fullscreen wordcount',
									menubar: 'edit format insert table',
									toolbar: 'fullscreen code | undo redo | styleselect | bold italic | bullist numlist outdent indent | link',

									setup: function( editor ) {

										// Update model on change
										editor.on( 'ExecCommand change NodeChange ObjectResized', function() {
											var content = editor.getContent();

											if ( ngModel.$dirty || content ) {
												$timeout( function() {
													ngModel.$setViewValue( content );
												} );
											}
										} );
									}
								} );
							} );
							break;
					}
				}
			};
		} );

})( angular );